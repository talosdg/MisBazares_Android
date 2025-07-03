package com.talos.misbazares.ui.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.talos.misbazares.data.db.model.EventEntity
import com.talos.misbazares.databinding.EventElementBinding

class EventsViewHolder(
    private val binding: EventElementBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(event: EventEntity){
        binding.apply{
            tvTitle.text = event.title
            tvAdmin.text = event.userId
            tvPlaces.text = event.places.toString()
            tvLocation.text = event.location
        }
    }
}