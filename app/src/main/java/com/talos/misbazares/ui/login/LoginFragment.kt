package com.talos.misbazares.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.talos.misbazares.MainActivity
import com.talos.misbazares.databinding.FragmentLoginBinding
import com.talos.misbazares.hideKeyboard
import com.talos.misbazares.SellersActivity
import com.talos.misbazares.application.EventsDBApp
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            (requireActivity().application as EventsDBApp).usersRepository,
            requireActivity().application
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btLogin.setOnClickListener {

            binding.root.hideKeyboard()

            val username = binding.etUser.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(username, password)
            } else {
                showSnackbar("Ingresa usuario y contraseña")
            }
        }

        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoginUiState.Success -> {
                    saveSession(state.userId, state.userRol)
                    navigateByRole(state.userId, state.userRol)
                }
                is LoginUiState.Error -> {
                    showSnackbar(state.message)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            val usersRepo = (requireActivity().application as EventsDBApp).usersRepository
            val allUsers = usersRepo.getAllUsers()
            Log.d("LoginDebug", "Usuarios en DB:")
            allUsers.forEach { Log.d("LoginDebug", it.toString()) }
        }
    }
    private fun saveSession(userId: Long, userRol: Int) {
        val sharedPrefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        sharedPrefs.edit()
            .putLong("userId", userId)
            .putInt("userRol", userRol)
            .apply()

        Log.d("Mi userId en LoginFragment fun saveSession: ", userId.toString())
        Log.d("Mi userRol en LoginFragment fun saveSession: ", userRol.toString())
    }

    private fun navigateByRole(userId: Long, rol: Int) {
        // Guardar sesión
        val sharedPrefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        sharedPrefs.edit()
            .putLong("userId", userId)
            .putInt("userRol", rol)
            .apply()

        Log.d("Mi userId en LoginFragment fun navigatByRol: ", userId.toString())
        Log.d("Mi rol en LoginFragment fun navigatByRol: ", rol.toString())

        val intent = if (rol == 2) {  // admin
            Intent(requireContext(), MainActivity::class.java)
        } else {  // vendedor
            Intent(requireContext(), SellersActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
