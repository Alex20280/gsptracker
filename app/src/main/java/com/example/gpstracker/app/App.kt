package com.example.gpstracker.app

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.gpstracker.di.AppComponent
import com.example.gpstracker.di.AppModule
import com.example.gpstracker.di.DaggerAppComponent
import javax.inject.Inject

class App : Application(), Configuration.Provider {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

       WorkManager.initialize(this, Configuration.Builder().build())
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
    }
}
