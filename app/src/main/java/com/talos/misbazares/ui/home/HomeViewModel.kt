package com.talos.misbazares.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talos.misbazares.data.EventsRepository
import com.talos.misbazares.data.UsersRepository
import com.talos.misbazares.data.db.model.EventEntity
import com.talos.misbazares.data.db.model.UsersEntity
import kotlinx.coroutines.launch

class HomeViewModel(
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _currentUser = MutableLiveData<UsersEntity?>()
    val currentUser: LiveData<UsersEntity?> get() = _currentUser

    fun loadUserById(userId: Long) {
        viewModelScope.launch {
            val user = usersRepository.getUserById(userId)
            _currentUser.postValue(user)
        }
    }
}
