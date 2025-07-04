package com.talos.misbazares.ui.sellers

import com.talos.misbazares.data.db.model.InscriptionEntity
import com.talos.misbazares.data.db.model.UsersEntity

data class SolicitudItem(
    val sellerName: String,
    val sellerId: Long,
    val eventTitle: String,
    val eventId: Int,
    val status: String,
    val inscription: InscriptionEntity,
    val vendedor: UsersEntity?
)
