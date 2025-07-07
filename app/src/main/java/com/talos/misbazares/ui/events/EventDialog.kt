package com.talos.misbazares.ui.events

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
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
import com.talos.misbazares.hideKeyboard
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate
import java.util.Locale

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

    private var selectedStartCalendar: Calendar? = null
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        super.onCreateDialog(savedInstanceState)
        _binding = EventsDialogBinding.inflate(requireActivity().layoutInflater)

        binding.apply {
            tietTitle.setText(event.title)
            tietAdmin.setText(event.userId)
            tietLocation.setText(event.location)
            tietPlaces.setText(event.places.toString())
            tieStartDate.setText(event.dateIni)
            tieEndDate.setText(event.dateEnd)
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
                    "Publicar", "Cancelar",
                    { actualizarEstado("publicado") },
                    { actualizarEstado("cancelado") }
                )
                "publicado" -> buildDialog(
                    "Dejar publicado", "Poner pendiente",
                    { actualizarEstado("publicado") },
                    { actualizarEstado("pendiente") }
                )
                "cancelado" -> buildDialog(
                    "Pasar a pendiente", "Eliminar",
                    { actualizarEstado("pendiente") },
                    { eliminarEvento() }
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
            event.places = tietPlaces.text.toString().toIntOrNull() ?: 10
            event.dateIni = tieStartDate.text.toString()
            event.dateEnd = tieEndDate.text.toString()
            event.status = status
        }
// fin datepicker ******
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
            event.dateIni = tieStartDate.text.toString()
            event.dateEnd = tieEndDate.text.toString()
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


        // datepicker ***********
        setupDateField(binding.tieStartDate) { showStartDatePicker() }
        setupDateField(binding.tieEndDate) { showEndDatePicker() }

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
                tietPlaces,
                tieStartDate,
                tieEndDate
            )
        }

    }

    private fun setupDateField(field: TextInputEditText, showPicker: () -> Unit) {
        // Deshabilitar el teclado
        field.inputType = InputType.TYPE_NULL
        field.keyListener = null
        field.isFocusable = true
        field.isFocusableInTouchMode = true

        // Click
        field.setOnClickListener {
            field.clearFocus()
            showPicker()
        }
        // Focus
        field.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                field.clearFocus()
                showPicker()
            }
        }
    }


    private fun showStartDatePicker() {
        val today = Calendar.getInstance()
        binding.root.hideKeyboard()
        DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            val selected = Calendar.getInstance()
            selected.set(year, month, dayOfMonth)
            selectedStartCalendar = selected // ✅ la guardamos

            val formatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selected.time)
            binding.tieStartDate.setText(formatted)
        },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showEndDatePicker() {
        val start = selectedStartCalendar ?: Calendar.getInstance()
        binding.root.hideKeyboard()
        DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            val selected = Calendar.getInstance()
            selected.set(year, month, dayOfMonth)

            val formatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selected.time)
            binding.tieEndDate.setText(formatted)
        },
            start.get(Calendar.YEAR),
            start.get(Calendar.MONTH),
            start.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun validateFields(): Boolean =

        binding.tietTitle.text.toString().isNotEmpty()
                && binding.tietLocation.text.toString().isNotEmpty()
              /*  && binding.tietAdmin.text.toString().isNotEmpty()*/
                && binding.tietPlaces.text.toString().isNotEmpty()
                && binding.tieStartDate.text.toString().isNotEmpty()
                && binding.tieEndDate.text.toString().isNotEmpty()

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