package com.talos.misbazares.ui.sellerevents

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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


    private lateinit var sellerEventsAdapter: SellerEventsAdapter


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

        sellerEventsAdapter = SellerEventsAdapter { selectedItem ->
            showEventDetail(selectedItem)
        }

        binding.rvEvents.adapter = sellerEventsAdapter


        binding.rvEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvents.adapter = sellerEventsAdapter

        viewModel.sellerEventsState.observe(viewLifecycleOwner) { state ->
            Log.d("SellersEventsFragment", "Eventos recibidos: ${state.disponibles.size}")
            renderizarSellerEventos(state)
        }

        // Simular ID del vendedor logueado
        val sellerId = 1L
        viewModel.loadSellerEvents(sellerId)

        viewModel.sellerEventsState.observe(viewLifecycleOwner) { state ->
            renderizarSellerEventos(state)
        }

    }

    private fun renderizarSellerEventos(state: SellerEventsUiState) {
        val items = mutableListOf<SellerEventItem>()
        items.addAll(state.disponibles.map { SellerEventItem(it, null) })
        items.addAll(state.solicitados.map { SellerEventItem(it, "solicitado") })
        items.addAll(state.aceptados.map { SellerEventItem(it, "aceptado") })
        items.addAll(state.cancelados.map { SellerEventItem(it, "cancelado") })

        sellerEventsAdapter.updateList(items)
    }

    private fun showEventDetail(item: SellerEventItem) {
        val event = item.event
        val sellerId = 1L

        val builder = AlertDialog.Builder(requireContext())
            .setTitle(event.title)
            .setMessage("""
            Admin: ${event.userId}
            Lugar: ${event.location}
            Cupos: ${event.places}
            Estado: ${item.inscriptionStatus ?: "disponible"}
        """.trimIndent())
            .setNegativeButton("Cerrar", null)
            .setNeutralButton("Ver en mapa") { _, _ ->
                val intent = Intent(requireContext(), SellerMapActivity::class.java)
                // Si quieres pasar datos del evento, puedes usar extras:
                intent.putExtra("eventId", event.id)
                intent.putExtra("location", event.location)
                startActivity(intent)
            }

        if (item.inscriptionStatus == null) {
            builder.setPositiveButton("Solicitar inscripción") { _, _ ->
                viewModel.insertInscription(event.id, sellerId, "solicitado")
                Toast.makeText(requireContext(), "Solicitud enviada", Toast.LENGTH_SHORT).show()
            }
        }

        builder.show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
