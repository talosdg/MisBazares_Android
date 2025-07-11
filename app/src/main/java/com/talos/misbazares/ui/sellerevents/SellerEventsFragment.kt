package com.talos.misbazares.ui.sellerevents

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
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
import com.google.android.material.snackbar.Snackbar
import com.talos.misbazares.LoginActivity
import com.talos.misbazares.R
import com.talos.misbazares.application.EventsDBApp
import com.talos.misbazares.confirmExitOnBackPress
import com.talos.misbazares.data.EventsRepository
import com.talos.misbazares.data.InscriptionRepository
import com.talos.misbazares.data.db.model.EventEntity
import com.talos.misbazares.databinding.FragmentSellerEventsBinding
import com.talos.misbazares.isNetworkAvailable
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

        confirmExitOnBackPress()

        sellerEventsAdapter = SellerEventsAdapter { selectedItem ->
            showEventDetail(selectedItem)
        }

        binding.rvEvents.adapter = sellerEventsAdapter
        binding.rvEvents.layoutManager = LinearLayoutManager(requireContext())

        viewModel.sellerEventsState.observe(viewLifecycleOwner) { state ->
            renderizarSellerEventos(state)
        }

        val sellerId = getSellerIdFromSession()
        viewModel.loadSellerEvents(sellerId)

        val sellerName = getSellerNameFromSession()
        binding.tvSellerName.text = "Eventos para $sellerName"

        binding.btCloseSession.setOnClickListener {
           mostrarDialogoCerrarSesion()
        }
    }

    private fun getSellerIdFromSession(): Long {
        val prefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        return prefs.getLong("userId", -1L)
    }

    private fun renderizarSellerEventos(state: SellerEventsUiState) {
        val items = mutableListOf<SellerEventItem>()
        items.addAll(state.disponibles.map { SellerEventItem(it, null) })
        items.addAll(state.solicitados.map { SellerEventItem(it, "solicitado") })
        items.addAll(state.aceptados.map { SellerEventItem(it, "aceptado") })
        items.addAll(state.cancelados.map { SellerEventItem(it, "cancelado") })

        binding.tvSinEventos.visibility = if (items.isNotEmpty()) View.INVISIBLE else View.VISIBLE

        sellerEventsAdapter.updateList(items)
    }

    private fun showEventDetail(item: SellerEventItem) {
        val event = item.event
        val sellerId = getSellerIdFromSession()

        val builder = AlertDialog.Builder(requireContext())
            .setTitle(event.title)
            .setMessage("""
                Dirección: ${event.location}
                Cupo: ${event.places} lugares
                Estatus: ${item.inscriptionStatus ?: "disponible"}
            """.trimIndent())
            .setNegativeButton("Cerrar", null)
            .setNeutralButton("Ver en mapa") { _, _ ->
                if (!isNetworkAvailable(requireContext())) {
                    Snackbar.make(binding.root, "Revise su conexión a internet", Snackbar.LENGTH_LONG).show()
                } else {
                    val intent = Intent(requireContext(), SellerMapActivity::class.java)
                    intent.putExtra("eventId", event.id)
                    intent.putExtra("location", event.location)
                    startActivity(intent)
                }
            }

        if (item.inscriptionStatus == null) {
            builder.setPositiveButton("Solicitar inscripción") { _, _ ->
                viewModel.insertInscription(event.id, sellerId, "solicitado")
                Toast.makeText(requireContext(), "Solicitud enviada", Toast.LENGTH_SHORT).show()
            }
        }

        builder.show()
    }

    private fun getSellerNameFromSession(): String {
        val sharedPrefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        return sharedPrefs.getString("userName", "Vendedor") ?: "Vendedor"
    }


    private fun mostrarDialogoCerrarSesion() {
        AlertDialog.Builder(requireContext())
            .setTitle("Cerrar sesión")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                cerrarSesion()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun cerrarSesion() {
        val prefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
