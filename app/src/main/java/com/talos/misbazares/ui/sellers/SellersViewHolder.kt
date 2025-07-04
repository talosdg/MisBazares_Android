package com.talos.misbazares.ui.sellers

import androidx.recyclerview.widget.RecyclerView
import com.talos.misbazares.data.db.model.UsersEntity
import com.talos.misbazares.databinding.SellerElementBinding

class SellersViewHolder(
    private val binding: SellerElementBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(seller: UsersEntity){ // aqui brinca de Users a Sellers


        var fullenameSeller = seller.name + " " + seller.secondname

        binding.apply{
            tvSellerName.text = fullenameSeller
        }
    }
}