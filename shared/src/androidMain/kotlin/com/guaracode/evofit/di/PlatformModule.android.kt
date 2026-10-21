package com.guaracode.evofit.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.guaracode.evofit.core.network.ConnectivityObserver
import com.guaracode.evofit.core.network.NetworkConnectivityObserver
import com.guaracode.evofit.data.local.AppDatabase
import com.guaracode.evofit.data.repository.SupportRepositoryImpl
import com.guaracode.evofit.domain.repository.SupportRepository
import okio.Path.Companion.toPath
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformDataModule = module {
    single<SupportRepository> { SupportRepositoryImpl(androidContext(), get()) }
    single<ConnectivityObserver> { NetworkConnectivityObserver(androidContext()) }
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                androidContext().filesDir.resolve("session.preferences_pb").absolutePath.toPath()
            }
        )
    }
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "evofit_database"
        ).fallbackToDestructiveMigration()
            .build()
    }
}
