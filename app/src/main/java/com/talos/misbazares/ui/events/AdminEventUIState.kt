package com.talos.misbazares.ui.events

import com.talos.misbazares.data.db.model.InscriptionEntity

data class AdminEventsUiState(
    val inscriptions: List<InscriptionEntity> = emptyList()
)

