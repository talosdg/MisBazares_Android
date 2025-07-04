package com.talos.misbazares.ui.sellerevents

import com.talos.misbazares.data.db.model.EventEntity

data class SellerEventItem(
    val event: EventEntity,
    val inscriptionStatus: String? // null = disponible
)



