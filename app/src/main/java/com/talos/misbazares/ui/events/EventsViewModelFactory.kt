package com.talos.misbazares.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.talos.misbazares.data.EventsRepository


class EventsViewModelFactory(
    private val eventsRepository: EventsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventsViewModel(eventsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
