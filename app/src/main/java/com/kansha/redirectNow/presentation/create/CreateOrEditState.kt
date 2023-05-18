package com.kansha.redirectNow.presentation.create

import com.kansha.redirectNow.data.model.PhoneDetails

sealed class CreateOrEditState {

    object Loading : CreateOrEditState()

    data class Edit(val phoneDetails: PhoneDetails) : CreateOrEditState()

    data class Save(val phoneDetails: PhoneDetails) : CreateOrEditState()

    data class Error(val errorMessage: String?) : CreateOrEditState()

}