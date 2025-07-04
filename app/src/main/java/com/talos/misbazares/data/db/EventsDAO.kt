package com.talos.misbazares.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.talos.misbazares.data.db.model.EventEntity
import com.talos.misbazares.util.Constants


@Dao
interface EventsDAO {
    // Create
    @Insert
    suspend fun insertEvent(event: EventEntity)

    @Insert
    suspend fun insertEvent(events: MutableList<EventEntity>)

    // Read
    @Query("SELECT * FROM ${Constants.DATABASE_EVENTS_TABLE}")
    suspend fun getAllEvents(): MutableList<EventEntity>

    @Query("SELECT * FROM ${Constants.DATABASE_EVENTS_TABLE } WHERE event_id = :eventId")
    suspend fun getAllEvents(eventId: Long): EventEntity?

    @Query("SELECT * FROM events_data_table WHERE event_status = :status")
    suspend fun getEventsWithStatus(status: String): List<EventEntity>

    @Query("SELECT * FROM ${Constants.DATABASE_EVENTS_TABLE} WHERE event_userId = :userId")
    suspend fun getEventsForUser(userId: String): List<EventEntity>

    @Query("SELECT * FROM ${Constants.DATABASE_EVENTS_TABLE} WHERE event_userId = :userId")
    suspend fun getEventsForAdmin(userId: String): List<EventEntity>

    @Query("SELECT * FROM ${Constants.DATABASE_EVENTS_TABLE} WHERE event_userId = :userId")
    suspend fun getAllEventsForUser(userId: String): MutableList<EventEntity>



    @Transaction
    // Update
    @Update
    suspend fun updateEvent(event: EventEntity)

    @Update
    suspend fun updateEvent(event: MutableList<EventEntity>)

    // Delete
    @Delete
    suspend fun deleteEvent(event: EventEntity)
}