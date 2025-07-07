package com.talos.misbazares.ui.sellers

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.talos.misbazares.data.InscriptionRepository

class SolicitudesViewModelFactory(
    private val application: Application,
    private val inscriptionRepository: InscriptionRepository
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SolicitudesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SolicitudesViewModel(application, inscriptionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}


