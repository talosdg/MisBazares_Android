package com.talos.misbazares.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class InscriptionDetail(
    @Embedded val inscription: InscriptionEntity,
    @ColumnInfo(name = "sellerName") val sellerName: String,
    @ColumnInfo(name = "eventTitle") val eventTitle: String
)
