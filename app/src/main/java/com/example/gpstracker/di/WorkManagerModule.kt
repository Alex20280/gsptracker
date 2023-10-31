package com.example.gpstracker.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.gpstracker.usecase.LocationTrackerUseCase
import com.example.gpstracker.usecase.workManager.CustomWorker
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.MapKey
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object WorkManagerModule {

    @Singleton
    @Provides
    fun provideWorkManager(context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    fun provideWorkerParameters(context: Context, workerParams: WorkerParameters): WorkerParameters {
        return workerParams
    }


    @Provides
    fun provideSyncDataUseCase(context: Context, workerParams: WorkerParameters): CustomWorker {
        return provideSyncDataUseCase(context, workerParams)
    }

    @Provides
    fun provideLocationTrackerUseCase(fusedLocationClient: FusedLocationProviderClient): LocationTrackerUseCase {
        return LocationTrackerUseCase(fusedLocationClient)
    }
}