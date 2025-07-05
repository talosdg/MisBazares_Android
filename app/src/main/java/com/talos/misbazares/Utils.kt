package com.talos.misbazares

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.FragmentManager
import com.talos.misbazares.ui.events.EventDialog


fun Activity.toast(text: String){
    Toast.makeText(
        this,
        text,
        Toast.LENGTH_SHORT
    ).show()
}

fun Fragment.showSnackbar(
    view: View,
    message: String,
    backgroundColorRes: Int,
    textColorRes: Int,
    duration: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(view, message, duration).apply {
        setBackgroundTint(ContextCompat.getColor(requireContext(), backgroundColorRes))
        setTextColor(ContextCompat.getColor(requireContext(), textColorRes))
        show()
    }
}
// Hidden Keybord
fun View.hideKeyboard(){
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}

// Back button do nothing
fun Fragment.disableBackPress(owner: LifecycleOwner = viewLifecycleOwner) {
    requireActivity().onBackPressedDispatcher.addCallback(owner, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // No hacer nada: se desactiva el botón Atrás
        }
    })
}

// Close app warning
fun Fragment.confirmExitOnBackPress(
    owner: LifecycleOwner = viewLifecycleOwner,
    title: String = "Cerrar Mis Bazares",
    message: String = "¿Seguro quieres cerrar la aplicación?"
) {
    requireActivity().onBackPressedDispatcher.addCallback(owner, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Sí") { _, _ ->
                    requireActivity().finish()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    })
}
fun showDetail(
    fragmentManager: FragmentManager,
    updateUI: () -> Unit,
    message: (String) -> Unit
) {
    val dialog = EventDialog(
        updateUI = updateUI,
        message = message
    )
    dialog.show(fragmentManager, "dialogo1")
}
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
