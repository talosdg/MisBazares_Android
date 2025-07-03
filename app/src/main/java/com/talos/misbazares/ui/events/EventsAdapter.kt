package com.talos.misbazares.ui.events

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.talos.misbazares.data.db.model.EventEntity
import com.talos.misbazares.databinding.EventElementBinding

class EventsAdapter(

    private val onEventClick : (EventEntity) -> Unit
): RecyclerView.Adapter<EventsViewHolder>() {

    // arreglo eventos
    private var events: List<EventEntity> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventsViewHolder {
        val binding = EventElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventsViewHolder(binding)
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(
        holder: EventsViewHolder,
        position: Int
    ) {
        val event = events[position]
        holder.bind(event)

        holder.itemView.setOnClickListener {
            //clic de cada elemento RECIBE LA LAMBDA
            onEventClick(event)
        }

    }
    fun updatelist(list: List<EventEntity>) {
        events = list.toMutableList()
        notifyDataSetChanged()
    }



}