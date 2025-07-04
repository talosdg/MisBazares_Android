package com.talos.misbazares.ui.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talos.misbazares.data.EventsRepository
import com.talos.misbazares.data.InscriptionRepository
import kotlinx.coroutines.launch

class AdminEventsViewModel (
    private val eventsRepository: EventsRepository,
    private val inscriptionRepository: InscriptionRepository
) : ViewModel() {

    private val _adminEventsState = MutableLiveData<AdminEventsUiState>()
    val adminEventsState: LiveData<AdminEventsUiState> = _adminEventsState

    fun loadAllInscriptions() {
        viewModelScope.launch {
            val inscriptions = inscriptionRepository.getAllInscriptions()
            _adminEventsState.postValue(AdminEventsUiState(inscriptions))
        }

    }

    fun approveInscription(eventId: Int, sellerId: Long) {
        viewModelScope.launch {
            inscriptionRepository.updateInscriptionStatus(eventId, sellerId, "aceptado")
            loadAllInscriptions()
        }
    }

    fun rejectInscription(eventId: Int, sellerId: Long) {
        viewModelScope.launch {
            inscriptionRepository.updateInscriptionStatus(eventId, sellerId, "cancelado")
            loadAllInscriptions()
        }
    }
}