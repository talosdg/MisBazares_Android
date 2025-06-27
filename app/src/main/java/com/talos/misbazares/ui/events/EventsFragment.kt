package com.talos.misbazares.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.talos.misbazares.application.EventsDBApp
import com.talos.misbazares.data.EventsRepository
import com.talos.misbazares.data.db.model.EventEntity
import com.talos.misbazares.databinding.FragmentEventsBinding
import kotlinx.coroutines.launch

class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!

    private var events: MutableList<EventEntity> = mutableListOf()
    private lateinit var repository: EventsRepository
    private lateinit var eventAdapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as EventsDBApp).repository

        eventAdapter = EventsAdapter { selectedEvent ->
            val dialog = EventDialog(
                newEvent = false,
                event = selectedEvent,
                updateUI = { updateUI() },
                message = { text -> message(text) }
            )
            dialog.show(parentFragmentManager, "dialog2")
        }

        binding.rvEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvents.adapter = eventAdapter

        updateUI()

        // Asumiendo que tienes un botón en el layout fragment_events.xml
        binding.fabAddEvent.setOnClickListener {
            createEvent()
        }
    }

    private fun updateUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            events = repository.getAllEvents()

            binding.tvSinRegistros.visibility =
                if (events.isNotEmpty()) View.INVISIBLE else View.VISIBLE

            eventAdapter.updatelist(events)
        }
    }

    private fun createEvent() {
        val dialog = EventDialog(
            updateUI = { updateUI() },
            message = { text -> message(text) }
        )
        dialog.show(parentFragmentManager, "dialogo1")
    }

    private fun message(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
