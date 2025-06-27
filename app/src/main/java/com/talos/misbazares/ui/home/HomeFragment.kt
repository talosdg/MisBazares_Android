package com.talos.misbazares.ui.home

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.MaterialColors
import com.talos.misbazares.MainActivity

import com.talos.misbazares.NewEventActivity
import com.talos.misbazares.R
import com.talos.misbazares.confirmExitOnBackPress
import com.talos.misbazares.databinding.FragmentHomeBinding
import com.talos.misbazares.disableBackPress
import com.talos.misbazares.ui.events.EventsFragment
import com.talos.misbazares.ui.managelists.SuppliersFragment

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
            textView.text = "Bienvenida Maribel " // dato dinámico it
        }

        binding.btTriggerEvent.setOnClickListener {
            val intent = Intent(binding.root.context, NewEventActivity::class.java)
            startActivity(intent)
        }

        binding.btViewEvent.setOnClickListener {
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNav.selectedItemId = R.id.navigation_events
        }

        binding.btSuppliers.setOnClickListener {

            val fragment = SuppliersFragment()

            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, fragment)
                .addToBackStack(null)
                .commit()

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
}