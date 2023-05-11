package com.kansha.phone2whats.data.local.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kansha.phone2whats.data.model.PhoneDetails

@Dao
interface PhoneDetailsDao {

    @Query("SELECT * FROM phonedetails")
    fun getAllPhone(): List<PhoneDetails>

    @Insert
    fun savePhone(phoneDetails: PhoneDetails)

    @Delete
    fun removePhone(phoneDetails: PhoneDetails)

    @Update
    fun updatePhone(phoneDetails: PhoneDetails)

    @Query("SELECT * FROM phonedetails WHERE id = :id")
    fun searchId(id: Int): PhoneDetails
}
