package com.talos.misbazares.ui.sellers

data class SolicitudItem(
    val sellerName: String,
    val sellerId: Long,
    val eventTitle: String,
    val eventId: Int,
    val status: String
)
