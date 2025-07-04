package com.talos.misbazares.ui.sellerevents

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.talos.misbazares.R
import com.talos.misbazares.application.EventsDBApp
import com.talos.misbazares.data.EventsRepository
import com.talos.misbazares.data.InscriptionRepository
import com.talos.misbazares.data.db.model.EventEntity
import com.talos.misbazares.databinding.FragmentSellerEventsBinding
import com.talos.misbazares.ui.events.EventsAdapter
import kotlinx.coroutines.launch

class SellersEventsFragment : Fragment() {

    private var _binding: FragmentSellerEventsBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventsAdapter: EventsAdapter

    private val viewModel: SellerEventsViewModel by viewModels {
        SellerEventsViewModelFactory(
            (requireActivity().application as EventsDBApp).eventsRepository,
            (requireActivity().application as EventsDBApp).inscriptionRepository
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.sellerEventsState.observe(viewLifecycleOwner) { state ->
            Log.d("SellersEventsFragment", "Eventos recibidos: ${state.disponibles.size}")
            renderizarEventos(state)
        }




        eventsAdapter = EventsAdapter { selectedEvent ->
            showEventDetail(selectedEvent)
        }

        binding.rvEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvents.adapter = eventsAdapter

        // Observa el estado
        viewModel.sellerEventsState.observe(viewLifecycleOwner) { state ->
            renderizarEventos(state)
        }

        // Simular ID del vendedor logueado
        val sellerId = 1L
        viewModel.loadSellerEvents(sellerId)
    }

    private fun renderizarEventos(state: SellerEventsUiState) {
        // Por ahora mostramos solo "disponibles"
        eventsAdapter.updatelist(state.disponibles)
    }

    private fun showEventDetail(event: EventEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle(event.title)
            .setMessage("""
                Admin: ${event.userId}
                Lugar: ${event.location}
                Cupos: ${event.places}
            """.trimIndent())
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
