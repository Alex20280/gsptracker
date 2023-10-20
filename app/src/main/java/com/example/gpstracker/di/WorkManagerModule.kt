package com.example.gpstracker.di

import android.content.Context
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.gpstracker.usecase.FirebaseDatabaseUseCase
import com.example.gpstracker.usecase.LocationTrackerUseCase
import com.example.gpstracker.usecase.SyncDatabaseUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.database.DatabaseReference
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
    fun provideSyncDataUseCase(context: Context, workerParams: WorkerParameters): SyncDatabaseUseCase {
        return provideSyncDataUseCase(context, workerParams)
    }

    @Provides
    fun provideLocationTrackerUseCase(fusedLocationClient: FusedLocationProviderClient): LocationTrackerUseCase {
        return LocationTrackerUseCase(fusedLocationClient)
    }
}