package com.talos.misbazares.ui.events

import android.content.Context
import android.os.Bundle
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
    val sharedPrefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)

    val userId = sharedPrefs.getLong("userId", -1L)
    val userRol = sharedPrefs.getInt("userRol", -1)


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



        eventAdapter = EventsAdapter { selectedEvent ->
            val dialog = EventDialog(
                newEvent = false,
                event = selectedEvent,
                updateUI = { updateUI() },
                message = { text -> message(text) }
            )
            dialog.show(parentFragmentManager, "dialog2")
        }

        viewModel.adminEventsLiveData.observe(viewLifecycleOwner) { events ->
            // Aquí actualizas el Adapter
            eventAdapter.updatelist(events.toMutableList())
        }

        // ejemplo: adminId obtenido de sesión
        val adminId = getAdminIdFromSession()
        viewModel.loadAdminEvents(adminId)




        binding.rvEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvents.adapter = eventAdapter

        updateUI()

        binding.fabAddEvent.setOnClickListener {
            showDetail(
                parentFragmentManager,
                updateUI = { updateUI() },
                message = { text -> message(text) }
            )
        }
    }

    private fun getAdminIdFromSession(): Long {
        val sharedPrefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        return sharedPrefs.getLong("userId", -1L)
    }

    private fun updateUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            events = repository.getAllEvents()

            binding.tvSinRegistros.visibility =
                if (events.isNotEmpty()) View.INVISIBLE else View.VISIBLE

            eventAdapter.updatelist(events)
        }
    }

    private fun message(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
