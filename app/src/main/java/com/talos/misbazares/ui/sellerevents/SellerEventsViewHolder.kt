package com.talos.misbazares.ui.sellerevents

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.talos.misbazares.R
import com.talos.misbazares.databinding.EventElementBinding

class SellerEventsViewHolder(
    private val binding: EventElementBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SellerEventItem) {
        val event = item.event
        binding.tvTitle.text = event.title
        binding.tvLocation.text = event.location
        binding.tvStatus.text = item.inscriptionStatus ?: "disponible"

        val color = when (item.inscriptionStatus) {
            "solicitado" -> ContextCompat.getColor(itemView.context, R.color.lighblue)
            "aceptado" -> ContextCompat.getColor(itemView.context, R.color.lighgreen)
            "cancelado" -> ContextCompat.getColor(itemView.context, R.color.lighred)
            else -> ContextCompat.getColor(itemView.context, R.color.lightyellow)
        }

        binding.cardView.setCardBackgroundColor(color)
    }
}
