package com.example.gpstracker.di

import android.content.Context
import com.example.gpstracker.prefs.UserPreferences
import dagger.Module
import dagger.Provides

@Module
object UserPreferenceModule {

    @Provides
    fun provideUserPreferences(context: Context): UserPreferences {
        return UserPreferences(context)
    }
}