package com.talos.misbazares.ui.sellers

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.talos.misbazares.data.InscriptionRepository
import com.talos.misbazares.data.db.model.InscriptionEntity
import kotlinx.coroutines.launch

class SolicitudesViewModel(
    application: Application,
    private val inscriptionRepository: InscriptionRepository
) : AndroidViewModel(application) {

    private val _solicitudes = MutableLiveData<List<InscriptionEntity>>()
    val solicitudes: LiveData<List<InscriptionEntity>> get() = _solicitudes

    fun loadSolicitudes(adminId: String) {
        viewModelScope.launch {
            val listaSolicitudes = inscriptionRepository.getSolicitudesForAdmin(adminId)
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
            val adminId = getAdminIdFromSession()
            loadSolicitudes(adminId)
        }
    }

    fun rechazarSolicitud(inscription: InscriptionEntity) {
        viewModelScope.launch {
            inscriptionRepository.updateInscriptionStatus(
                eventId = inscription.eventId,
                sellerId = inscription.sellerId,
                newStatus = "rechazado"
            )
            val adminId = getAdminIdFromSession()
            loadSolicitudes(adminId)
        }
    }
    private fun getAdminIdFromSession(): String {
        val prefs = getApplication<Application>().getSharedPreferences("session", Context.MODE_PRIVATE)
        return prefs.getLong("userId", -1L).toString()
    }

}
