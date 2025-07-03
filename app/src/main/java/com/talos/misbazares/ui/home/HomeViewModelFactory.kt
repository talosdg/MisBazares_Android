package com.talos.misbazares.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.talos.misbazares.data.UsersRepository

class HomeViewModelFactory(
    private val usersRepository: UsersRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(usersRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
