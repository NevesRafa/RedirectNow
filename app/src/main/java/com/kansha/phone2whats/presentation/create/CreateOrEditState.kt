package com.kansha.phone2whats.presentation.create

import com.kansha.phone2whats.data.model.PhoneDetails

sealed class CreateOrEditState {

    object Loading : CreateOrEditState()

    data class Edit(val phoneDetails: PhoneDetails) : CreateOrEditState()

    data class Save(val phoneDetails: PhoneDetails) : CreateOrEditState()

    data class Error(val errorMessage: String?) : CreateOrEditState()

}