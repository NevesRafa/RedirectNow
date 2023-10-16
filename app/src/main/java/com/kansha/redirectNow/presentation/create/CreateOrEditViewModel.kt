package com.kansha.redirectNow.presentation.create

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kansha.redirectNow.data.model.PhoneDetails
import com.kansha.redirectNow.domain.countryList

class CreateOrEditViewModel : ViewModel() {

    private var isEdit: Boolean = false
    private var contactForEdit: PhoneDetails? = null
    private var ddiTyped: String? = null

    val loadStateLiveData = MutableLiveData<CreateOrEditState>()
    val phoneMaskLiveData = MutableLiveData<TextWatcher>()

    fun checksSaveOrEdit(
        contactTyped: String,
        ddiTyped: String,
        phoneNumberTyped: String,
        flagCode: String
    ) {
        val isValidDdi = isDdiValid(ddiTyped)

        if (isValidDdi) {
            val contact: PhoneDetails

            if (!isEdit) {
                contact = createNewContact(contactTyped, ddiTyped, phoneNumberTyped, flagCode)
            } else {
                contact = changeContactForEdit(contactTyped, ddiTyped, phoneNumberTyped, flagCode)
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

    private fun changeContactForEdit(
        contactTyped: String,
        ddiTyped: String,
        phoneNumberTyped: String,
        flagCodeTyped: String
    ): PhoneDetails {

        contactForEdit?.apply {
            contact = contactTyped
            ddi = ddiTyped
            phoneNumber = phoneNumberTyped
            flagCode = flagCodeTyped
        }
        return contactForEdit!!
    }

    private fun createNewContact(
        contactTyped: String,
        ddiTyped: String,
        phoneNumberTyped: String,
        flagCode: String
    ): PhoneDetails {
        return PhoneDetails(
            id = 0,
            contact = contactTyped,
            ddi = ddiTyped,
            phoneNumber = phoneNumberTyped,
            flagCode = flagCode
        )
    }

    fun isEditing(loadedContact: PhoneDetails?) {
        contactForEdit = loadedContact

        if (contactForEdit != null) {
            isEdit = true
            loadStateLiveData.postValue(CreateOrEditState.Edit(contactForEdit!!))
        }
    }

    private fun createMaskFormatter(mask: String): TextWatcher {

        return object : TextWatcher {
            private var isFormatting: Boolean = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                if (isFormatting) {
                    return
                }

                isFormatting = true

                val phone = editable.toString().replace("\\D".toRegex(), "")
                val maskedString = StringBuilder()
                var phoneIndex = 0

                for (i in mask.indices) {
                    if (phoneIndex >= phone.length) {
                        break
                    }
                    if (mask[i] == '#') {
                        maskedString.append(phone[phoneIndex])
                        phoneIndex++
                    } else {
                        maskedString.append(mask[i])
                    }
                }

                editable?.replace(0, editable.length, maskedString.toString())

                isFormatting = false
            }
        }
    }

    fun setDdi(ddi: String) {
        ddiTyped = ddi

        val country = countryList.find { it.countryCode == ddiTyped }
        val mask = country?.mask ?: ""

        if (mask == "") {
            return
        }

        phoneMaskLiveData.value = createMaskFormatter(mask)
    }

    fun getFlagCode(ddiTyped: String): String {
        val code = countryList.find { it.countryCode == ddiTyped }
        return code?.flagCode ?: ""

    }
}