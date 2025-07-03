package com.talos.misbazares.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.talos.misbazares.util.Constants

@Entity(tableName = Constants.DATABASE_USERS_TABLE)
data class UsersEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var id: Long = 0,
    @ColumnInfo(name = "user_rol")
    var rol: Int = 0,
    @ColumnInfo(name = "user_name")
    var name: String,
    @ColumnInfo(name = "user_secondname")
    var secondname: String,
    @ColumnInfo(name = "user_email")
    var email: String,
    @ColumnInfo(name = "user_events")
    var events: String,
    @ColumnInfo(name = "user_password")
    var password: String,
)