package com.kansha.redirectNow.internal

import androidx.room.Room
import com.kansha.redirectNow.data.local.AppDatabase
import com.kansha.redirectNow.domain.PhoneRepository
import com.kansha.redirectNow.presentation.create.CreateOrEditViewModel
import com.kansha.redirectNow.presentation.home.HomeViewModel
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