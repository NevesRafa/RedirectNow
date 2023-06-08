package com.kansha.redirectNow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kansha.redirectNow.data.local.dao.PhoneDetailsDao
import com.kansha.redirectNow.data.model.PhoneDetails

@Database(entities = [PhoneDetails::class], version = 2, exportSchema = true)

abstract class AppDatabase : RoomDatabase() {
    abstract fun phoneDetailsDao(): PhoneDetailsDao
}