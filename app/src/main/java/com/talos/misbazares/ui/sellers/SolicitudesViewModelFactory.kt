package com.talos.misbazares.ui.sellers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.talos.misbazares.data.InscriptionRepository

class SolicitudesViewModelFactory(
    private val inscriptionRepository: InscriptionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SolicitudesViewModel(inscriptionRepository) as T
    }
}


