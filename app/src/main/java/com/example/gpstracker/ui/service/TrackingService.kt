package com.example.gpstracker.ui.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.usecase.FirebaseDatabaseUseCase
import com.example.gpstracker.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrackingService : Service() {

    @Inject
    lateinit var locationServiceUseCase: FirebaseDatabaseUseCase
    private val serviceScope = CoroutineScope(Dispatchers.Main)
    private var locationJob: Job? = null
    private val NOTIFICATION_ID = 1234

    override fun onCreate() {
        super.onCreate()
        (application as App).appComponent.inject(this)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun startServiceTracking() {
        locationJob = serviceScope.launch {
            while (isActive) {
                locationServiceUseCase.saveLocation()
                delay(com.example.gpstracker.BuildConfig.TRACKING_INTERVAL_MILLIS)
            }
        }
    }

    private fun stopService(){
        serviceScope.cancel()
    }

    override fun onDestroy() {
        stopForeground(true)
        stopSelf()
        serviceScope.cancel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> {
                start()
                startServiceTracking()
            }

            Actions.STOP.toString() -> {
                stopService()
                stopSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, Utils.CHANNEL_ID)
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

