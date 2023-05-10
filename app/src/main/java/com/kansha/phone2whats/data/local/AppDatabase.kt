package com.kansha.phone2whats.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kansha.phone2whats.data.local.dao.PhoneDetailsDao
import com.kansha.phone2whats.data.model.PhoneDetails

@Database(entities = [PhoneDetails::class], version = 1, exportSchema = true)

abstract class AppDatabase : RoomDatabase() {

    abstract fun phoneDetailsDao(): PhoneDetailsDao
}