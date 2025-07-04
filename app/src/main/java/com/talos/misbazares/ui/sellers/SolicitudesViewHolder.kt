package com.talos.misbazares.ui.sellers

import androidx.recyclerview.widget.RecyclerView
import com.talos.misbazares.databinding.ItemSolicitudBinding

class SolicitudesViewHolder(
    private val binding: ItemSolicitudBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(inscription: SolicitudItem) {
        binding.tvEventId.text = "Event ID: ${inscription.eventId}"
        binding.tvSellerId.text = "Seller ID: ${inscription.sellerId}"
        binding.tvStatus.text = "Status: ${inscription.status}"
    }
}
