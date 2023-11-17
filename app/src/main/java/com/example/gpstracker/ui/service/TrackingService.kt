package com.example.gpstracker.ui.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.usecase.FirebaseDatabaseUseCase
import javax.inject.Inject

class TrackingService : Service() {

    @Inject
    lateinit var locationServiceUseCase: FirebaseDatabaseUseCase


    private val NOTIFICATION_ID = 1234
    private val CHANNEL_ID = "running_channel"

    override fun onCreate() {
        super.onCreate()
        (application as App).appComponent.inject(this)
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    private fun startServiceTracking() {
        locationServiceUseCase.saveLocation()
    }


    override fun onDestroy() {
        stopForeground(true)
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> {
                start()
                startServiceTracking()
            }

            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Location Tracking")
            .setContentText("Tracking location updates")
            .setSmallIcon(R.drawable.track_point)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    enum class Actions {
        START, STOP
    }
}

