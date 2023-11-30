package com.example.gpstracker.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.gpstracker.app.App
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module
class AppModule(private val application: App) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideViewModelMap(): Map<Class<out ViewModel>, Provider<ViewModel>> {
        return mutableMapOf()
    }
}

