package com.talos.misbazares.ui.sellerevents

import com.talos.misbazares.data.db.model.EventEntity

data class SellerEventsUiState(
    val disponibles: List<EventEntity> = emptyList(),
    val solicitados: List<EventEntity> = emptyList(),
    val aceptados: List<EventEntity> = emptyList(),
    val cancelados: List<EventEntity> = emptyList()
)
