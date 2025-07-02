package com.talos.misbazares.ui.home

import android.content.Intent
import android.media.MediaSyncEvent.createEvent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.talos.misbazares.NewEventActivity
import com.talos.misbazares.R
import com.talos.misbazares.confirmExitOnBackPress
import com.talos.misbazares.databinding.FragmentHomeBinding
import com.talos.misbazares.showDetail

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        val textView: TextView = binding.tvWelcome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = "Bienvenido XYZ " // dato dinámico it
        }

        binding.btTriggerEvent.setOnClickListener {
            showDetail(
                parentFragmentManager,
                updateUI = { updateUI() },
                message = { text -> message("Se generó el evento") }
            )
        }

        binding.btViewEvent.setOnClickListener {
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNav.selectedItemId = R.id.navigation_events
        }

        binding.btSellers.setOnClickListener {
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNav.selectedItemId = R.id.navigation_sellers
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        confirmExitOnBackPress()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUI() {
        // Aquí actualiza la UI del HomeFragment
    }

    private fun message(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}