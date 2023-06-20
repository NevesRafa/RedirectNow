package com.kansha.redirectNow.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class PhoneDetails(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var contact: String,
    var ddi: String,
    var flagCode: String,
    var phoneNumber: String
) : Parcelable