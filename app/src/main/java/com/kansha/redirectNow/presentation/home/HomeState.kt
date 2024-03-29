package com.kansha.redirectNow.presentation.home

import android.view.View
import com.kansha.redirectNow.data.model.PhoneDetails

sealed class HomeState {

    object Loading : HomeState()

    data class EditOrRemove(val itemClick: View, val phoneDetails: PhoneDetails) : HomeState()

    data class OpenModalEdit(val phoneDetails: PhoneDetails) : HomeState()

    data class Success(val result: List<PhoneDetails>) : HomeState()

    data class Error(val errorMessage: String?) : HomeState()
}