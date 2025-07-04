package com.talos.misbazares.ui.events

import android.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.talos.misbazares.R
import com.talos.misbazares.data.db.model.EventEntity
import com.talos.misbazares.databinding.EventElementBinding

class EventsViewHolder(
    private val binding: EventElementBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(event: EventEntity){
        binding.apply{
            tvTitle.text = event.title
            tvAdmin.text = "Admin: ${event.userId}"
            tvPlaces.text = event.places.toString()
            tvLocation.text = event.location
            tvStatus.text = event.status
        }
        // Elegir color según status
        val color = when (event.status) {
            "publicado" -> Color.parseColor("#C8E6C9")     // verde claro
            "pendiente" -> Color.parseColor("#FFF9C4")     // amarillo claro
            "cancelado" -> Color.parseColor("#FFCDD2")     // rojo claro
            "solicitado" -> Color.parseColor("#BBDEFB")    // azul claro
            "aceptado" -> Color.parseColor("#A5D6A7")      // verde más fuerte
            else -> Color.WHITE
        }

        // Cambiar fondo del ConstraintLayout dentro del Card
        //binding.clContainer.setBackgroundColor(color)
        binding.cardView.setCardBackgroundColor(color)
    }

}