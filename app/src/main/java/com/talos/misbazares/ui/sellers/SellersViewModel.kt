package com.talos.misbazares.ui.sellers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.talos.misbazares.data.UsersRepository
import com.talos.misbazares.data.InscriptionRepository
import com.talos.misbazares.data.db.model.InscriptionEntity
import com.talos.misbazares.data.db.model.UsersEntity
import kotlinx.coroutines.launch

class SellersViewModel(
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _sellers = MutableLiveData<List<UsersEntity>>()
    val sellers: LiveData<List<UsersEntity>> get() = _sellers

    private val _sellersLiveData = MutableLiveData<List<UsersEntity>>()
    val sellersLiveData: LiveData<List<UsersEntity>> = _sellersLiveData

    private val _sellerSolicitudes = MutableLiveData<List<InscriptionEntity>>()
    val sellerSolicitudes: LiveData<List<InscriptionEntity>> = _sellerSolicitudes

    fun loadSellers() {
        viewModelScope.launch {
            val sellers = usersRepository.getAllSellers()
            _sellersLiveData.postValue(sellers)
        }
    }


}
