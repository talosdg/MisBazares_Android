package com.talos.misbazares.ui.events

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.talos.misbazares.R
import com.talos.misbazares.data.db.model.EventEntity
import com.talos.misbazares.databinding.EventElementBinding
import com.talos.misbazares.ui.sellerevents.SellerEventItem

class EventsViewHolder(
    private val binding: EventElementBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(event: EventEntity) {
        binding.tvTitle.text = event.title
        binding.tvLocation.text = event.location
        binding.tvStatus.text = event.status

        val color = when (event.status) {
            "publicado" -> R.color.lighgreen
            "pendiente" -> R.color.lightyellow
            "cancelado" -> R.color.lighred
            else -> R.color.white
        }

        binding.cardView.setCardBackgroundColor(
            ContextCompat.getColor(itemView.context, color)
        )
    }

}
