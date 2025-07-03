package com.talos.misbazares.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.talos.misbazares.util.Constants

@Entity(
    tableName = Constants.DATABASE_INSCRIPTIONS_TABLE,
    primaryKeys = ["event_id", "seller_id"],
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["event_id"],
            childColumns = ["event_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsersEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["seller_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class InscriptionEntity(
    @ColumnInfo(name = "event_id")
    val eventId: Int,

    @ColumnInfo(name = "seller_id")
    val sellerId: Long,

    val status: String
)
