package com.example.gpstracker.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides

@Module
object WorkManagerModule {

    @Provides
    fun provideWorkManager(context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}