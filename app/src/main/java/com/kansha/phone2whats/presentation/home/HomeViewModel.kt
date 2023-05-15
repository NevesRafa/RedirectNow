package com.kansha.phone2whats.presentation.home

import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kansha.phone2whats.R
import com.kansha.phone2whats.data.model.PhoneDetails
import com.kansha.phone2whats.domain.PhoneRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val repository: PhoneRepository) : ViewModel() {

    val loadStateLiveData = MutableLiveData<HomeState>()

    fun loadList() {
        viewModelScope.launch {

            loadStateLiveData.postValue(HomeState.Loading)

            try {
                val phoneList = withContext(Dispatchers.IO) {
                    repository.getPhonesList()
                }
                loadStateLiveData.postValue(HomeState.Success(phoneList))
            } catch (error: Exception) {
                loadStateLiveData.postValue(HomeState.Error(error.message))
            }
        }
    }

    fun clickMenu(menuItem: MenuItem, phone: PhoneDetails) {

        if (menuItem.itemId == R.id.menu_remove) {
            removePhone(phone)
            loadList()
        } else if (menuItem.itemId == R.id.menu_editar) {
            loadStateLiveData.postValue(HomeState.OpenModalEdit(phone))
        }
    }

    private fun removePhone(phone: PhoneDetails) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.removePhone(phone)
                loadList()
            }
        }
    }

    fun phoneEdit(phoneEdited: PhoneDetails) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.phoneEdit(phoneEdited)
                loadList()
            }
        }
    }

    fun savePhone(phone: PhoneDetails) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.phoneSave(phone)
                loadList()
            }
        }
    }
}