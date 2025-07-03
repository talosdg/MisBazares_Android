package com.talos.misbazares.ui.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.talos.misbazares.data.UsersRepository

class LoginViewModelFactory(
    private val usersRepository: UsersRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(usersRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

