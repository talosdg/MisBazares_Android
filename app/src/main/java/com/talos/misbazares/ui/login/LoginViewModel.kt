package com.talos.misbazares.ui.login

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.talos.misbazares.application.EventsDBApp
import com.talos.misbazares.data.UsersRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val usersRepository: UsersRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _loginState = MutableLiveData<LoginUiState>()
    val loginState: LiveData<LoginUiState> get() = _loginState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = usersRepository.login(username, password)
            Log.d("LoginViewModel", "Usuario encontrado: $user")
            if (user != null) {
                _loginState.postValue(LoginUiState.Success(user.id, user.rol))
            } else {
                _loginState.postValue(LoginUiState.Error("Usuario o contraseña incorrectos"))
            }
        }
    }
}
sealed class LoginUiState {
    data class Success(val userId: Long, val userRol: Int) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

