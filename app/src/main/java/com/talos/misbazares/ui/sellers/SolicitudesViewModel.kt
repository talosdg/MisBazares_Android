package com.talos.misbazares.ui.sellers

import androidx.lifecycle.*
import com.talos.misbazares.data.InscriptionRepository
import com.talos.misbazares.data.db.model.InscriptionEntity
import kotlinx.coroutines.launch

class SolicitudesViewModel(
    private val inscriptionRepository: InscriptionRepository,

) : ViewModel() {

    private val _solicitudes = MutableLiveData<List<InscriptionEntity>>()
    val solicitudes: LiveData<List<InscriptionEntity>> get() = _solicitudes

    fun loadSolicitudes() {
        viewModelScope.launch {
            val listaSolicitudes = inscriptionRepository.getAllSolicitudes()
            _solicitudes.postValue(listaSolicitudes)
        }
    }

    fun aprobarSolicitud(inscription: InscriptionEntity) {
        viewModelScope.launch {
            inscriptionRepository.updateInscriptionStatus(
                eventId = inscription.eventId,
                sellerId = inscription.sellerId,
                newStatus = "aceptado"
            )
            loadSolicitudes()
        }
    }

    fun rechazarSolicitud(inscription: InscriptionEntity) {
        viewModelScope.launch {
            inscriptionRepository.updateInscriptionStatus(
                eventId = inscription.eventId,
                sellerId = inscription.sellerId,
                newStatus = "rechazado"
            )
            loadSolicitudes()
        }
    }



}
