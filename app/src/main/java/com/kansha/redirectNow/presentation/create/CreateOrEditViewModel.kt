package com.kansha.redirectNow.presentation.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kansha.redirectNow.data.model.PhoneDetails

class CreateOrEditViewModel : ViewModel() {

    private var isEdit: Boolean = false
    private var contactForEdit: PhoneDetails? = null
    val loadStateLiveData = MutableLiveData<CreateOrEditState>()

    fun checksSaveOrEdit(contactTyped: String, phoneNumberTyped: String) {
        val contact: PhoneDetails

        if (!isEdit) {
            contact = createNewContact(contactTyped, phoneNumberTyped)
        } else {
            contact = changeContactForEdit(contactTyped, phoneNumberTyped)
        }
        loadStateLiveData.postValue(CreateOrEditState.Save(contact))
    }

    private fun changeContactForEdit(contactTyped: String, phoneNumberTyped: String): PhoneDetails {

        contactForEdit?.apply {
            contact = contactTyped
            phoneNumber = phoneNumberTyped
        }
        return contactForEdit!!
    }

    private fun createNewContact(contactTyped: String, phoneNumberTyped: String): PhoneDetails {
        return PhoneDetails(
            id = 0,
            contact = contactTyped,
            phoneNumber = phoneNumberTyped
        )
    }

    fun isEditing(loadedContact: PhoneDetails?) {
        contactForEdit = loadedContact

        if (contactForEdit != null) {
            isEdit = true
            loadStateLiveData.postValue(CreateOrEditState.Edit(contactForEdit!!))
        }
    }
}