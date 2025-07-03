package com.talos.misbazares.ui.sellerevents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.talos.misbazares.data.EventsRepository
import com.talos.misbazares.data.InscriptionRepository

class SellerEventsViewModelFactory(
    private val eventsRepository: EventsRepository,
    private val inscriptionRepository: InscriptionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SellerEventsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SellerEventsViewModel(eventsRepository, inscriptionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
