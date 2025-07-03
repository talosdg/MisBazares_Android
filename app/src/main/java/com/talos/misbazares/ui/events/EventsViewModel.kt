package com.talos.misbazares.ui.events

import androidx.lifecycle.*
import com.talos.misbazares.data.EventsRepository
import com.talos.misbazares.data.db.model.EventEntity
import kotlinx.coroutines.launch
class EventsViewModel(
    private val eventsRepository: EventsRepository
) : ViewModel() {

    private val _eventsLiveData = MutableLiveData<List<EventEntity>>()
    val eventsLiveData: LiveData<List<EventEntity>> get() = _eventsLiveData

    // Para user
    fun loadUserEvents(userId: String) {
        viewModelScope.launch {
            val events = eventsRepository.getEventsForUser(userId)
            _eventsLiveData.postValue(events)
        }
    }

    // Para Seller
    fun loadPublishedEvents() {
        viewModelScope.launch {
            val events = eventsRepository.getEventsWithStatus("publicado")
            _eventsLiveData.postValue(events)
        }
    }

    // General
    fun refreshAllEvents() {
        viewModelScope.launch {
            val allEvents = eventsRepository.getAllEvents()
            _eventsLiveData.postValue(allEvents)
        }
    }
}

