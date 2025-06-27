package com.talos.misbazares.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.talos.misbazares.MainActivity
import com.talos.misbazares.R
import com.talos.misbazares.databinding.FragmentLoginBinding
import com.talos.misbazares.databinding.FragmentRegisterBinding
import com.talos.misbazares.hideKeyboard
import com.talos.misbazares.showSnackbar

import android.widget.Toast
import androidx.core.widget.doOnTextChanged

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        val roles = resources.getStringArray(R.array.roles)
        val gender = resources.getStringArray(R.array.gender)
        val arrayAdapterRol = ArrayAdapter(requireContext(), R.layout.drop_rol_item, roles)
        val arrayAdapterGender = ArrayAdapter(requireContext(), R.layout.drop_gender_item, gender)



       binding.autoSelectedRol.apply{
            setAdapter(arrayAdapterRol)
           /* doOnTextChanged { selectedText, _, _, _ ->
                Toast.makeText(requireContext(), "Prelegido: $selectedText", Toast.LENGTH_SHORT).show()
            }*/
        }
        binding.autoSelectedGender.apply{
            setAdapter(arrayAdapterGender)
            /* doOnTextChanged { selectedText, _, _, _ ->
                 Toast.makeText(requireContext(), "Prelegido: $selectedText", Toast.LENGTH_SHORT).show()
             }*/
        }
 /*
        binding.testSpinner.setOnClickListener {
            val selectedText = binding.autoSelectedRol.text.toString()
            if (selectedText.isEmpty()) {
                Toast.makeText(requireContext(), "Selecciona un valor", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Elegido: $selectedText", Toast.LENGTH_SHORT).show()
            }
        }
*/




        binding.btSave.setOnClickListener {
            val fields = listOf(
                binding.etUser,
                binding.autoSelectedRol,
                binding.etName,
                binding.etSecondName,
                binding.etEmail,
                binding.etPhone,
                binding.autoSelectedGender,
                binding.etPassword,
                binding.etConfirmPass
            )

            val errorColor = ContextCompat.getColor(requireContext(), R.color.text_error)
            val normalHintColor = ContextCompat.getColor(requireContext(), R.color.text_hint)
            val normalColor = ContextCompat.getColor(requireContext(), R.color.black)

            var allValid = true

            view?.hideKeyboard()

            // Validar campos vacíos y resetear estados
            fields.forEach { field ->
                val text = field.text.toString().trim()
                if (text.isEmpty()) {
                    allValid = false
                    field.setHintTextColor(errorColor)
                    field.error = "Dato requerido"
                } else {
                    field.setHintTextColor(normalHintColor)
                    field.error = null
                }
            }

            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()
            val confirmPass = binding.etConfirmPass.text.toString().trim()
            val selectedRol = binding.autoSelectedRol.text.toString().trim()
            val selectedGender = binding.autoSelectedGender.text.toString().trim()

            if (selectedRol.isEmpty()) {
                allValid = false
                binding.autoSelectedRol.error = "Elije tu rol"
            } else {
               // binding.autoSelectedRol.error = null
               // Toast.makeText(requireContext(), "Elegido: $selectedRol", Toast.LENGTH_SHORT).show()
            }

            // Email
            val emailPattern = android.util.Patterns.EMAIL_ADDRESS
            if (email.isNotEmpty() && !emailPattern.matcher(email).matches()) {
                allValid = false
                binding.etEmail.error = "El formato del correo es inválido"
            }

            // Phone
            if (phone.isNotEmpty() && phone.length < 10) {
                allValid = false
                binding.etPhone.error = "El número telefónico debe tener 10 dígitos"
            }
            // Gender
            if (selectedGender.isEmpty()) {
                allValid = false
                binding.autoSelectedGender.error = "Elije tu género"
            } else {
                //Toast.makeText(requireContext(), "Elegido: $selectedGender", Toast.LENGTH_SHORT).show()
            }

            // Password length
            if (pass.isNotEmpty() && pass.length < 8) {
                allValid = false
                binding.etPassword.error = "La contraseña debe tener al menos 8 caracteres"
            }

            // Password match
            if (pass.isNotEmpty() && confirmPass.isNotEmpty() && pass != confirmPass) {
                allValid = false
                binding.etConfirmPass.error = "Las contraseñas no coinciden"
            }


            if (!allValid) {
                showSnackbar(
                    binding.root,
                    getString(R.string.enter_requiredReg),
                    R.color.snakbar_red,
                    R.color.white
                )
            } else {
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
        }




        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}