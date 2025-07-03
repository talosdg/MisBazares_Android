package com.talos.misbazares.ui.sellers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.talos.misbazares.data.db.model.UsersEntity
import com.talos.misbazares.databinding.SellerElementBinding

class SellersAdapter (
private val onSellerClick : (UsersEntity) -> Unit
): RecyclerView.Adapter<SellersViewHolder>() {

    // arreglo vendedores
    private var sellers: List<UsersEntity> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SellersViewHolder {
        val binding =
            SellerElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SellersViewHolder(binding)
    }

    override fun getItemCount(): Int = sellers.size

    override fun onBindViewHolder(
        holder: SellersViewHolder,
        position: Int
    ) {
        val seller = sellers[position]
        holder.bind(seller)

        holder.itemView.setOnClickListener {
            //clic de cada elemento RECIBE LA LAMBDA
            onSellerClick(seller)
        }

    }

    fun updatelist(list: MutableList<UsersEntity>) {
        sellers = list
        notifyDataSetChanged()
    }
}