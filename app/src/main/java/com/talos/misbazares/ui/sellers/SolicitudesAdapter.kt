package com.talos.misbazares.ui.sellers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.talos.misbazares.databinding.ItemSolicitudBinding

class SolicitudesAdapter(
    private val onClick: (SolicitudItem) -> Unit
) : RecyclerView.Adapter<SolicitudesViewHolder>() {

    private var items: List<SolicitudItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolicitudesViewHolder {
        val binding = ItemSolicitudBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SolicitudesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SolicitudesViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<SolicitudItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
