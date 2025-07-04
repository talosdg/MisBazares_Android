package com.talos.misbazares.ui.sellerevents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.talos.misbazares.databinding.EventElementBinding

class SellerEventsAdapter(
    private val onEventClick: (SellerEventItem) -> Unit
) : RecyclerView.Adapter<SellerEventsViewHolder>() {

    private var items: List<SellerEventItem> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SellerEventsViewHolder {
        val binding = EventElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SellerEventsViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(
        holder: SellerEventsViewHolder,
        position: Int
    ) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onEventClick(item)
        }
    }

    fun updateList(list: List<SellerEventItem>) {
        items = list
        notifyDataSetChanged()
    }
}
