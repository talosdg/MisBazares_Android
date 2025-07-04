package com.talos.misbazares.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.talos.misbazares.util.Constants

@Entity(tableName = Constants.DATABASE_EVENTS_TABLE)
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "event_id")
    var id: Int = 0,
    @ColumnInfo(name = "event_title")
    var title: String,
    @ColumnInfo(name = "event_location")
    var location: String,
    @ColumnInfo(name = "event_userId")
    var userId: String, // <- clave para filtrar
    @ColumnInfo(name = "event_places")
    var places: Int = 0,
    @ColumnInfo(name = "event_dateIni")
    var dateIni: String,
    @ColumnInfo(name = "event_dateEnd")
    var dateEnd: String,
    @ColumnInfo(name = "event_status")
    var status: String
)