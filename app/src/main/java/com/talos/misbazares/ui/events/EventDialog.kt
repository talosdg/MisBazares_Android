package com.talos.misbazares.ui.events

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.talos.misbazares.application.EventsDBApp
import com.talos.misbazares.data.EventsRepository
import com.talos.misbazares.data.db.model.EventEntity
import com.talos.misbazares.databinding.EventsDialogBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException

class EventDialog(
    private val newEvent: Boolean = true, // aviso de existencia de entidad
    private var event: EventEntity = EventEntity(
        title = "",
        userId = "",
        location = "",
        places = 0,
        dateIni = "",
        dateEnd = "",
        status = ""
    ),
    private val updateUI: () -> Unit, // funcion de actualizacion como parametro
    private val  message: (String) -> Unit // Lambda par ano perder contexto de dialog en dialog
): DialogFragment() {

    private var _binding: EventsDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: EventsRepository

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        super.onCreateDialog(savedInstanceState)
        _binding = EventsDialogBinding.inflate(requireActivity().layoutInflater)

        binding.apply {
            tietTitle.setText(event.title)
            tietAdmin.setText(event.userId)
            tietLocation.setText(event.location)
            tietPlaces.setText(event.places.toString())
        }

        dialog = if(newEvent)
            buildDialog("Guardar", "Cancelar", {
                // Guardar
                binding.apply{
                    event.title = tietTitle.text.toString()
                    event.userId = tietAdmin.text.toString()
                    event.location = tietLocation.text.toString()
                    event.places = tietPlaces.text.toString().toIntOrNull() ?: 0

                }
                try{
                    lifecycleScope.launch {
                        val result =async {
                            repository.insertEvent(event)
                        }

                        result.await() //espera la construcción completa de la vista
                        message("El evento se ha generado")

                        updateUI() // función pasada por parametro lambda
                    }
                }catch (e: IOException){
                    e.printStackTrace()
                    message("Error al guardar el evento")

                }catch (e: IOException){
                    e.printStackTrace()
                }

            }, {
                // Cancelar
            })
        else
            buildDialog("Actualizar", "Eliminar", {
                // Actualizar
                binding.apply{
                    event.title = tietTitle.text.toString()
                    event.userId = tietAdmin.text.toString()
                    event.location = tietLocation.text.toString()
                    event.places = tietPlaces.text.toString().toIntOrNull() ?: 0

                }
                try{
                    lifecycleScope.launch {
                        val result =async {
                            repository.updateEvent(event)
                        }

                        result.await() //espera la construcción completa de la vista
                        message("El evento se ha actualizado")

                        updateUI() // función pasada por parametro lambda
                    }
                }catch (e: IOException){
                    e.printStackTrace()
                    message("Error al actualizar el evento")

                }catch (e: IOException){
                    e.printStackTrace()
                }
            }, {
                // Eliminar
                AlertDialog.Builder(requireContext())
                    .setTitle("Confirmación")
                    .setMessage("¿Realmente quieres eliminar el evento ${event.title}?")
                    .setPositiveButton("Aceptar") {_, _ ->
                        try{
                            lifecycleScope.launch {
                                val result =async {
                                    repository.deleteEvent(event)
                                }

                                result.await() //espera la construcción completa de la vista
                                message("El evento se ha eliminado") // funcion de toaster

                                updateUI() // función pasada por parametro lambda
                            }
                        }catch (e: IOException){
                            e.printStackTrace()
                            message("Error al eliminar el evento")

                        }catch (e: IOException){
                            e.printStackTrace()
                        }
                    }
                    .setNegativeButton("Cancelar") {  dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .create()
                    .show()
            })

        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStart() {
        super.onStart()

        // instancia del repo acceso desde fragment requireContext().applicationContext
        repository = (requireContext().applicationContext as EventsDBApp).eventsRepository

        saveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false

        binding.apply{
            setupTextWatcher(
                tietTitle,
                tietLocation,
                tietAdmin,
                tietPlaces // falta booleano de shareable
            )
        }

    }



    private fun validateFields(): Boolean =

        binding.tietTitle.text.toString().isNotEmpty()
                && binding.tietLocation.text.toString().isNotEmpty()
              /*  && binding.tietAdmin.text.toString().isNotEmpty()*/
                && binding.tietPlaces.text.toString().isNotEmpty()

    private fun setupTextWatcher(vararg textField: TextInputEditText){
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        }

        textField.forEach { it.addTextChangedListener(textWatcher) }
    }

    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: ()-> Unit,
        negativeButton: ()-> Unit
    ): Dialog =
        AlertDialog.Builder(requireContext()).setView(binding.root)
            .setTitle("Evento")
            .setPositiveButton(btn1Text) { _, _ ->
                positiveButton()
            }
            .setNegativeButton(btn2Text) { _, _ ->
                negativeButton()
            }
            .create()
}