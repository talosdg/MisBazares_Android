package com.talos.misbazares.ui.detailevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.talos.misbazares.data.EventsRepository
import com.talos.misbazares.data.db.model.EventEntity

sealed class EventDialogAction {
    data class ShowDialog(val newEvent: Boolean, val event: EventEntity? = null) : EventDialogAction()
}
class DetailViewModel(
    private val eventsRepository: EventsRepository // Puedes inyectar repositorios si el diálogo los necesita
) : ViewModel() {

    // LiveData que el Fragment observará para saber cuándo mostrar el diálogo
    private val _showEventDialog = MutableLiveData<EventDialogAction?>()
    val showEventDialog: LiveData<EventDialogAction?> = _showEventDialog

    fun createEvent() {
        _showEventDialog.value = EventDialogAction.ShowDialog(newEvent = true)
    }

    fun editEvent(event: EventEntity) {
        _showEventDialog.value = EventDialogAction.ShowDialog(newEvent = false, event = event)
    }

    fun eventDialogShown() {
        _showEventDialog.value = null // Reinicia el valor para que no se active de nuevo
    }

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun postMessage(text: String) {
        _message.value = text
    }
}