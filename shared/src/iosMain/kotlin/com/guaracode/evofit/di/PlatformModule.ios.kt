package com.guaracode.evofit.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.guaracode.evofit.core.network.ConnectivityObserver
import com.guaracode.evofit.core.network.NetworkConnectivityObserver
import com.guaracode.evofit.data.local.AppDatabase
import com.guaracode.evofit.data.local.AppDatabaseConstructor
import com.guaracode.evofit.data.repository.SupportRepositoryImpl
import com.guaracode.evofit.domain.repository.SupportRepository
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual val platformDataModule = module {
    single<SupportRepository> { SupportRepositoryImpl(get()) }
    single<ConnectivityObserver> { NetworkConnectivityObserver() }
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = true,
                    error = null
                )
                ((documentDirectory?.path ?: "") + "/session.preferences_pb").toPath()
            }
        )
    }
    single<AppDatabase> {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null
        )
        val dbFilePath = (documentDirectory?.path ?: "") + "/evofit_database.db"
        Room.databaseBuilder<AppDatabase>(
            name = dbFilePath,
            factory = { AppDatabaseConstructor.initialize() }
        ).setDriver(BundledSQLiteDriver())
            .build()
    }
}
