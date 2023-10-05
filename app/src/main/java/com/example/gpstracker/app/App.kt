package com.example.gpstracker.app

import android.app.Application
import com.example.gpstracker.di.AppComponent
import com.example.gpstracker.di.AppModule
import com.example.gpstracker.di.DaggerAppComponent

class App  : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}