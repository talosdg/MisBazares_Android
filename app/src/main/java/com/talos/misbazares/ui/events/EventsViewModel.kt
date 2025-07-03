package com.talos.misbazares.ui.events

import androidx.lifecycle.*
import com.talos.misbazares.data.EventsRepository
import com.talos.misbazares.data.db.model.EventEntity
import kotlinx.coroutines.launch

class EventsViewModel(
    private val eventsRepository: EventsRepository
) : ViewModel() {

    private val _adminEventsLiveData = MutableLiveData<List<EventEntity>>()
    val adminEventsLiveData: LiveData<List<EventEntity>> get() = _adminEventsLiveData

    fun loadAdminEvents(adminId: Long) {
        viewModelScope.launch {
            val events = eventsRepository.getEventsForAdmin(adminId)
            _adminEventsLiveData.postValue(events)
        }
    }

    fun refreshAllEvents() {
        viewModelScope.launch {
            val allEvents = eventsRepository.getAllEvents()
            _adminEventsLiveData.postValue(allEvents)
        }
    }
}
