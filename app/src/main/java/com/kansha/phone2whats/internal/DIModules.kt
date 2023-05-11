package com.kansha.phone2whats.internal

import androidx.room.Room
import com.kansha.phone2whats.data.local.AppDatabase
import com.kansha.phone2whats.domain.PhoneRepository
import com.kansha.phone2whats.presentation.create.CreateOrEditViewModel
import com.kansha.phone2whats.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

const val DATABASE_NAME = "phone2whats.db"

val appModule = module {

    //database
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    single { get<AppDatabase>().phoneDetailsDao() }

    // repository
    factory { PhoneRepository(get()) }

    //viewmodels
    viewModel { HomeViewModel(get()) }
    viewModel { CreateOrEditViewModel() }
}