package com.example.gpstracker.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.gpstracker.base.extentions.myDataStore
import dagger.Module
import dagger.Provides

@Module
object DataPreferenceModule {

    @Provides
    fun provideDataStorePreferences(context: Context): DataStore<Preferences> {
        return context.myDataStore
    }
}