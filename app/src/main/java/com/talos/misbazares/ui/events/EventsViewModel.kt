package com.talos.misbazares.ui.events


import androidx.lifecycle.*
import com.talos.misbazares.data.EventsRepository
import com.talos.misbazares.data.InscriptionRepository
import com.talos.misbazares.data.db.model.EventEntity
import kotlinx.coroutines.launch

class EventsViewModel(
    private val repository: EventsRepository
) : ViewModel() {

    private val _eventsLiveData = MutableLiveData<MutableList<EventEntity>>()
    val eventsLiveData: LiveData<MutableList<EventEntity>> = _eventsLiveData

    fun loadUserEvents(userId: String) {
        viewModelScope.launch {
            val eventos = repository.getAllEventsForUser(userId)
            _eventsLiveData.postValue(eventos)
        }
    }

    fun loadAllEvents() {
        viewModelScope.launch {
            val eventos = repository.getAllEvents()
            _eventsLiveData.postValue(eventos)
        }
    }
}


