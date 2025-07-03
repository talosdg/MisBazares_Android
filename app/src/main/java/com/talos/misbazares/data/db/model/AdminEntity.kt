package com.talos.misbazares.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.talos.misbazares.util.Constants

@Entity(tableName = Constants.DATABASE_ADMIN_TABLE)
data class AdminEntity(
    @PrimaryKey val adminId: String,
    val name: String
)
