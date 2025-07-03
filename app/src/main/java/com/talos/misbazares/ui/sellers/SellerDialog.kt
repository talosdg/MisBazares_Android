package com.talos.misbazares.ui.sellers

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.talos.misbazares.data.db.model.UsersEntity

class SellerDialog(
    private val seller: UsersEntity,
    private val message: (String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Vendedor")
            .setMessage("Nombre: ${seller.name} ${seller.secondname}")
            .setPositiveButton("OK") { _, _ -> }
            .create()
    }
}
