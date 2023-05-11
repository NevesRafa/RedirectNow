package com.kansha.phone2whats.domain

import com.kansha.phone2whats.data.local.dao.PhoneDetailsDao
import com.kansha.phone2whats.data.model.PhoneDetails

class PhoneRepository(private val database: PhoneDetailsDao) {

    fun getPhonesList(): List<PhoneDetails> {
        return database.getAllPhone()
    }

    fun removePhone(phone: PhoneDetails) {
        database.removePhone(phone)
    }

    fun phoneEdit(phone: PhoneDetails) {
        database.updatePhone(phone)
    }

    fun phoneSave(phone: PhoneDetails) {
        database.savePhone(phone)
    }
}