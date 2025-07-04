package com.talos.misbazares.ui.sellers

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.talos.misbazares.application.EventsDBApp
import com.talos.misbazares.data.db.model.UsersEntity
import com.talos.misbazares.databinding.FragmentSellersBinding


class SellersFragment : Fragment() {

    private var _binding: FragmentSellersBinding? = null
    private val binding get() = _binding!!

    private lateinit var sellersAdapter: SellersAdapter
    private lateinit var solicitudesAdapter: SolicitudesAdapter

    private val sellersViewModel: SellersViewModel by viewModels {
        SellersViewModelFactory(
            (requireActivity().application as EventsDBApp).usersRepository
        )
    }

    private val solicitudesViewModel: SolicitudesViewModel by viewModels {
        SolicitudesViewModelFactory(
            (requireActivity().application as EventsDBApp).inscriptionRepository
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa adaptadores con sus lambdas de click
        sellersAdapter = SellersAdapter { seller ->
            showSellerDetail(seller)
        }

        solicitudesAdapter = SolicitudesAdapter { solicitud ->
            solicitudesViewModel.aprobarSolicitud(solicitud)
        }

        // Configura RecyclerViews
        binding.rvSellers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSellers.adapter = sellersAdapter

        binding.rvSolicitudes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSolicitudes.adapter = solicitudesAdapter

        // Observa LiveData de ViewModel
        sellersViewModel.sellers.observe(viewLifecycleOwner) { lista ->
            sellersAdapter.updatelist(lista.toMutableList())
        }

        // Carga datos al entrar al fragment
        sellersViewModel.loadSellers()
        solicitudesViewModel.loadSolicitudes()
    }

    private fun showSellerDetail(seller: UsersEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Detalles del vendedor")
            .setMessage(
                """
                Nombre: ${seller.name} ${seller.secondname}
                Rol: ${seller.rol}
            """.trimIndent()
            )
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun aprobarSolicitud(solicitud: SolicitudItem) {
        AlertDialog.Builder(requireContext())
            .setTitle("Aprobar solicitud")
            .setMessage(
                """
                Vendedor: ${solicitud.sellerName}
                Evento ID: ${solicitud.eventId}
                Status actual: ${solicitud.status}
            """.trimIndent()
            )
            .setPositiveButton("Aprobar") { _, _ ->
                solicitudesViewModel.aprobarSolicitud(solicitud)
                Toast.makeText(requireContext(), "Solicitud aprobada", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
