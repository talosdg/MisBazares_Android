package com.talos.misbazares.ui.sellerevents

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talos.misbazares.data.EventsRepository
import com.talos.misbazares.data.InscriptionRepository
import com.talos.misbazares.data.db.model.EventEntity
import com.talos.misbazares.data.db.model.InscriptionEntity
import kotlinx.coroutines.launch



class SellerEventsViewModel(
    private val eventsRepository: EventsRepository,
    private val inscriptionRepository: InscriptionRepository
) : ViewModel() {

    private val _sellerEventsState = MutableLiveData<SellerEventsUiState>()
    val sellerEventsState: LiveData<SellerEventsUiState> = _sellerEventsState

    fun loadSellerEvents(sellerId: Long) {
        viewModelScope.launch {
            val events = eventsRepository.getEventsWithStatus("publicado")
            val inscriptions = inscriptionRepository.getInscriptionsForSeller(sellerId)
            val state = classifyEvents(events, inscriptions)
            _sellerEventsState.postValue(state)

            Log.d("EventosViewModel", "Inscriptions: ${inscriptions.size}")

            //Log.d("EventosViewModel", "Clasificados: disponibles=${disponibles.size}, solicitados=${solicitados.size}")
            Log.d("EventosViewModel", "Evento status=${events}")
           // Log.d("SellerEventsFragment", "Eventos renderizados: ${eventsAdapter.itemCount}")

        }


    }

    private fun classifyEvents(
        events: List<EventEntity>,
        inscriptions: List<InscriptionEntity>
    ): SellerEventsUiState {
        val map = inscriptions.associateBy { it.eventId }

        val disponibles = mutableListOf<EventEntity>()
        val solicitados = mutableListOf<EventEntity>()
        val aceptados = mutableListOf<EventEntity>()
        val cancelados = mutableListOf<EventEntity>()

        for (event in events) {
            val inscripcion = map[event.id]
            if (inscripcion == null) {
                disponibles.add(event)
            } else {
                when (inscripcion.status) {
                    "solicitado" -> solicitados.add(event)
                    "aceptado" -> aceptados.add(event)
                    "cancelado" -> cancelados.add(event)
                }
            }
        }

        return SellerEventsUiState(
            disponibles,
            solicitados,
            aceptados,
            cancelados
        )
    }


    fun insertInscription(eventId: Int, sellerId: Long, status: String) {
        viewModelScope.launch {
            inscriptionRepository.insertInscription(
                InscriptionEntity(
                    eventId = eventId,
                    sellerId = sellerId,
                    status = status
                )
            )
            loadSellerEvents(sellerId)
        }
    }
    fun updateInscriptionStatus(eventId: Int, sellerId: Long, newStatus: String) {
        viewModelScope.launch {
            inscriptionRepository.updateInscriptionStatus(eventId, sellerId, newStatus)
            loadSellerEvents(sellerId)
        }
    }


}
