package com.talos.misbazares.ui.sellers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.talos.misbazares.data.db.model.InscriptionEntity
import com.talos.misbazares.databinding.ItemSolicitudBinding

class SolicitudesAdapter(
    private val onClick: (InscriptionEntity) -> Unit
) : RecyclerView.Adapter<SolicitudesViewHolder>() {

    private var solicitudes: List<InscriptionEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolicitudesViewHolder {
        val binding = ItemSolicitudBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SolicitudesViewHolder(binding)
    }

    override fun getItemCount(): Int = solicitudes.size

    override fun onBindViewHolder(holder: SolicitudesViewHolder, position: Int) {
        val item = solicitudes[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

    fun updateList(list: List<InscriptionEntity>) {
        solicitudes = list
        notifyDataSetChanged()
    }
}




