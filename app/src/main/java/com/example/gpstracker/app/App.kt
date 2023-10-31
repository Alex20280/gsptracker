package com.example.gpstracker.app

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.example.gpstracker.di.AppComponent
import com.example.gpstracker.di.AppModule
import com.example.gpstracker.di.DaggerAppComponent
import com.example.gpstracker.usecase.workManager.SampleWorkerFactory
import javax.inject.Inject

class App : Application(), Configuration.Provider { // Configuration.Provider

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var workerFactory: SampleWorkerFactory

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

/*        val workManagerConfig = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        WorkManager.initialize(this, workManagerConfig)*/
       WorkManager.initialize(this, Configuration.Builder().build())
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
    }
}
