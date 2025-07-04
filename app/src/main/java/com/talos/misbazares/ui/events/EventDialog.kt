package com.talos.misbazares.ui.events

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
        status = "pendiente"
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

        dialog = if (newEvent) {
            AlertDialog.Builder(requireContext())
                .setTitle("Nuevo evento")
                .setView(binding.root)
                .setPositiveButton("Publicar") { _, _ ->
                    guardarNuevoEventoConStatus("publicado")
                }
                .setNeutralButton("Dejar pendiente") { _, _ ->
                    guardarNuevoEventoConStatus("pendiente")
                }
                .setNegativeButton("Cancelar", null)
                .create()
        } else {
            when (event.status) {
                "pendiente" -> buildDialog(
                    "Publicar", "Cancelar ",
                    { actualizarEstado("publicado") },
                    { /*desistir*/  }
                )
                "publicado" -> buildDialog(
                    "Dejar pendiente", "Cancelar evento",
                    { actualizarEstado("pendiente") },
                    { actualizarEstado("cancelado") }
                )
                "cancelado" -> buildDialog(
                    "Eliminar", "Cancelar",
                    { eliminarEvento() },
                    { /* cerrar */ }
                )
                else -> buildDialog(
                    "Actualizar", "Eliminar",
                    { actualizarEstado(event.status ?: "pendiente") },
                    { eliminarEvento() }
                )
            }
        }

        return dialog
    }

    private fun guardarNuevoEventoConStatus(status: String) {
        binding.apply {
            event.title = tietTitle.text.toString()
            event.userId = getUserIdFromSession().toString()
            event.location = tietLocation.text.toString()
            event.places = tietPlaces.text.toString().toIntOrNull() ?: 0
            event.status = status
        }

        try {
            lifecycleScope.launch {
                repository.insertEvent(event)
                Log.d("DebugEventos", "Guardando evento como $status desde EventDialog con userId=${event.userId}  ")
                message("El evento se ha generado como $status")
                event.userId = getUserIdFromSession().toString()
                updateUI()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            message("Error al guardar el evento")
        }
    }
    private fun getUserIdFromSession(): Long {
        val prefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        return prefs.getLong("userId", -1L)
    }


    private fun actualizarEstado(nuevoStatus: String) {
        binding.apply {
            event.title = tietTitle.text.toString()
            event.userId = tietAdmin.text.toString()
            event.location = tietLocation.text.toString()
            event.places = tietPlaces.text.toString().toIntOrNull() ?: 0
            event.status = nuevoStatus
        }

        lifecycleScope.launch {
            repository.updateEvent(event)
            message("Evento actualizado a $nuevoStatus")
            updateUI()
        }
    }

    private fun eliminarEvento() {
        lifecycleScope.launch {
            repository.deleteEvent(event)
            message("Evento eliminado")
            updateUI()
        }
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
        // saveButton?.isEnabled = false EL ORIGINAL
        saveButton?.isEnabled = validateFields()

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