package com.talos.misbazares.ui.events

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.talos.misbazares.application.EventsDBApp
import com.talos.misbazares.data.EventsRepository
import com.talos.misbazares.data.db.model.EventEntity
import com.talos.misbazares.databinding.FragmentEventsBinding
import com.talos.misbazares.showDetail
import kotlinx.coroutines.launch

class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!

    private var events: MutableList<EventEntity> = mutableListOf()
    private lateinit var repository: EventsRepository
    private lateinit var eventAdapter: EventsAdapter

    private val viewModel: EventsViewModel by viewModels {
        EventsViewModelFactory(
            (requireActivity().application as EventsDBApp).eventsRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as EventsDBApp).eventsRepository

        val userRol = getUserRolFromSession()
        val userId = getUserIdFromSession()

        viewModel.loadUserEvents(userId.toString())

        eventAdapter = EventsAdapter { selectedEvent ->
            val dialog = EventDialog(
                newEvent = false,
                event = selectedEvent,
                updateUI = { updateUI() },
                message = { text -> message(text) }
            )
            dialog.show(parentFragmentManager, "dialog2")
        }
        val userIdLong = getUserIdFromSession()
        val userIdString = userIdLong.toString()

        Log.d("DebugEventos", "ID en sesión (Long) = $userIdLong")
        Log.d("DebugEventos", "ID en sesión (String) = $userIdString")

        viewModel.loadUserEvents(userIdString)
        viewModel.eventsLiveData.observe(viewLifecycleOwner) { events ->
            Log.d("DebugEventos", "Eventos recibidos (${events.size}):")
            events.forEach {
                Log.d("DebugEventos", "Evento: ${it.title}, userId=${it.userId}")
            }

            updateUI()
            eventAdapter.updatelist(events.toMutableList())
        }

        binding.rvEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvents.adapter = eventAdapter

        /*viewModel.eventsLiveData.observe(viewLifecycleOwner) { events ->
            binding.tvSinRegistros.visibility =
                if (events.isNotEmpty()) View.INVISIBLE else View.VISIBLE
            eventAdapter.updateList(events)
        }*/

        binding.fabAddEvent.setOnClickListener {
            showDetail(
                parentFragmentManager,
                updateUI = { updateUI() },
                message = { text -> message(text) }
            )
        }
        
    }

    private fun updateUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            val userId = getUserIdAsString()
            events = repository.getEventsForAdmin(userId)
            binding.tvSinRegistros.visibility =
                if (events.isNotEmpty()) View.INVISIBLE else View.VISIBLE
            eventAdapter.updatelist(events)
        }
    }
    private fun getUserIdAsString(): String {
        val id = getUserIdFromSession()
        return id.toString()
    }

    private fun message(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun getUserIdFromSession(): Long {
        val prefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        return prefs.getLong("userId", -1L)
    }

    private fun getUserRolFromSession(): Int {
        val prefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        return prefs.getInt("userRol", -1)
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
