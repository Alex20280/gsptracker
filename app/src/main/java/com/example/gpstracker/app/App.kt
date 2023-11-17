package com.example.gpstracker.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.gpstracker.di.AppComponent
import com.example.gpstracker.di.AppModule
import com.example.gpstracker.di.DaggerAppComponent

class App : Application(), Configuration.Provider {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        WorkManager.initialize(this, Configuration.Builder().build())

        initNotificationChannel()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
    }

    private fun initNotificationChannel() {
        val channel = NotificationChannel(
            "running_channel",
            "Running notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
