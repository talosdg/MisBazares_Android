package com.talos.misbazares.ui.home

import android.content.Context
import android.content.Intent
import android.media.MediaSyncEvent.createEvent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.talos.misbazares.NewEventActivity
import com.talos.misbazares.R
import com.talos.misbazares.application.EventsDBApp
import com.talos.misbazares.confirmExitOnBackPress
import com.talos.misbazares.databinding.FragmentHomeBinding
import com.talos.misbazares.showDetail

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    val viewModel: HomeViewModel by viewModels() {
        HomeViewModelFactory(
            (requireActivity().application as EventsDBApp).usersRepository
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        val textView: TextView = binding.tvWelcome


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

        val userId = getUserIdFromSession()
        viewModel.loadUserById(userId)

        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.tvWelcome.text = "${user.name} ${user.secondname}"
               // binding.tvUserRole.text = if (user.rol == 2) "Admin" else "Seller"
            } else {
                // Maneja error de usuario no encontrado
            }
        }
    }

    private fun getUserIdFromSession(): Long {
        val sharedPrefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        return sharedPrefs.getLong("userId", -1L)
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