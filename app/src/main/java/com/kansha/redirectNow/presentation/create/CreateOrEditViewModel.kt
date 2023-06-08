package com.kansha.redirectNow.presentation.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kansha.redirectNow.data.model.PhoneDetails
import com.kansha.redirectNow.domain.countryList

class CreateOrEditViewModel : ViewModel() {

    private var isEdit: Boolean = false
    private var contactForEdit: PhoneDetails? = null
    val loadStateLiveData = MutableLiveData<CreateOrEditState>()


    fun checksSaveOrEdit(contactTyped: String, ddiTyped: String, phoneNumberTyped: String) {
        val isValidDdi = isDdiValid(ddiTyped)

        if (isValidDdi) {
            val contact: PhoneDetails

            if (!isEdit) {
                contact = createNewContact(contactTyped, ddiTyped, phoneNumberTyped)
            } else {
                contact = changeContactForEdit(contactTyped, ddiTyped, phoneNumberTyped)
            }
            loadStateLiveData.postValue(CreateOrEditState.Save(contact))
        } else {
            loadStateLiveData.postValue(CreateOrEditState.InvalidDdi)
        }
    }

    private fun isDdiValid(ddi: String): Boolean {
        val ddiList = countryList.map { it.countryCode }
        return ddiList.contains(ddi)
    }

    private fun changeContactForEdit(contactTyped: String, ddiTyped: String, phoneNumberTyped: String): PhoneDetails {

        contactForEdit?.apply {
            contact = contactTyped
            ddi = ddiTyped
            phoneNumber = phoneNumberTyped
        }
        return contactForEdit!!
    }

    private fun createNewContact(contactTyped: String, ddiTyped: String, phoneNumberTyped: String): PhoneDetails {
        return PhoneDetails(
            id = 0,
            contact = contactTyped,
            ddi = ddiTyped,
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