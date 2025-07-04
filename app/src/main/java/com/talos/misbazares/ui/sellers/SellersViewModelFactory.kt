package com.talos.misbazares.ui.sellers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.talos.misbazares.data.UsersRepository


class SellersViewModelFactory(
    private val userRepository: UsersRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SellersViewModel(userRepository) as T
    }
}



