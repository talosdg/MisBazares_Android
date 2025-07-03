package com.talos.misbazares.ui.sellers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.talos.misbazares.data.db.model.UsersEntity
import com.talos.misbazares.databinding.FragmentSellersBinding
import com.talos.misbazares.ui.login.UserAuth

class SellersFragment : Fragment() {

    private var _binding: FragmentSellersBinding? = null
    private val binding get() = _binding!!

    private lateinit var sellersAdapter: SellersAdapter
    private var sellers: MutableList<UsersEntity> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sellersAdapter = SellersAdapter { selectedSeller ->
            val dialog = SellerDialog(
                seller = selectedSeller,
                message = { msg -> showMessage(msg) }
            )
            dialog.show(parentFragmentManager, "dialogoSeller")
        }

        binding.rvSellers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSellers.adapter = sellersAdapter

        updateUI()
    }

    private fun updateUI() {
        sellers = UserAuth.getAllSellers().toMutableList()
        sellersAdapter.updatelist(sellers)
    }


    private fun showMessage(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun getFakeSellers(): MutableList<UsersEntity> {
        return mutableListOf(
            UsersEntity(name = "Juana",  secondname = "López", email ="mimail@mail.com",  rol = 2, events ="", password = "clave123"),
            UsersEntity(name = "Carlos", secondname = "Pérez",  email ="mimail@mail.com", rol = 2, events ="", password = "clave123"),
            UsersEntity(name = "María",  secondname = "Gómez", email ="mimail@mail.com",  rol = 2, events ="", password = "clave123")
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
