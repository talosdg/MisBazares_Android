package com.talos.misbazares.data

import com.talos.misbazares.data.db.EventsDAO
import com.talos.misbazares.data.db.model.EventEntity

class EventsRepository(
    private val eventsDAO: EventsDAO
) {
    suspend fun insertEvent(event: EventEntity){
        eventsDAO.insertEvent(event)
    }
    suspend fun getAllEvents(): MutableList<EventEntity> =
        eventsDAO.getAllEvents()

    suspend fun updateEvent(event: EventEntity){
        eventsDAO.updateEvent(event)
    }
    suspend fun deleteEvent(event: EventEntity){
        eventsDAO.deleteEvent(event)
    }
}