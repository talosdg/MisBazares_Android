package com.talos.misbazares.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.snackbar.Snackbar
import com.talos.misbazares.MainActivity
import com.talos.misbazares.R
import com.talos.misbazares.databinding.FragmentLoginBinding
import com.talos.misbazares.hideKeyboard
import com.talos.misbazares.showSnackbar
import com.talos.misbazares.toast


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.btLogin.setOnClickListener { view ->

            val loginText = binding.etUser.text.toString().trim()
            val passwordText = binding.etPassword.text.toString().trim()

            view.hideKeyboard()

           /* if (loginText.isEmpty() || passwordText.isEmpty()) {
                showSnackbar(
                    binding.root,
                    getString(R.string.enter_required),
                    R.color.snakbar_red,
                    R.color.white
                )
            }else
            {*/
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
         /*   }*/



        }

        binding.btRegister.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                replace<RegisterFragment>(R.id.login_container)
                setReorderingAllowed(true)
                addToBackStack("replacement")
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


