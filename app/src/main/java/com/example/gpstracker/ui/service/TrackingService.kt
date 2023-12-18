package com.example.gpstracker.ui.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewModelScope
import com.example.gpstracker.R
import com.example.gpstracker.app.App
import com.example.gpstracker.roomdb.LocationModel
import com.example.gpstracker.roomdb.LocationRepository
import com.example.gpstracker.usecase.FirebaseDatabaseUseCase
import com.example.gpstracker.usecase.ReceiveCurrentLocationUseCase
import com.example.gpstracker.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class TrackingService : Service() {

    @Inject
    lateinit var locationServiceUseCase: FirebaseDatabaseUseCase

    @Inject
    lateinit var currentLocationUseCase: ReceiveCurrentLocationUseCase

    @Inject
    lateinit var locationTracker: LocationRepository

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

/*    private fun saveToFirebase() {
        locationJob = serviceScope.launch {
            while (isActive) {
                locationServiceUseCase.saveLocationToFirebase()
                delay(com.example.gpstracker.BuildConfig.TRACKING_INTERVAL_MILLIS)
            }
        }
    }*/

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
                checkInternetAvailability()

            }

            Actions.STOP.toString() -> {
                stopService()
                stopSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun checkInternetAvailability() {
        locationJob = serviceScope.launch {
            while (isActive) {
                val isInternetConnected = isInternetConnected()
                if (isInternetConnected) {
                    saveToFirebase()
                } else {
                    saveToRoomDb()
                }
                delay(com.example.gpstracker.BuildConfig.TRACKING_INTERVAL_MILLIS)
            }
        }
    }

    private fun saveToFirebase() {
        serviceScope.launch {
            locationServiceUseCase.saveLocationToFirebase()
        }
    }

    private fun saveToRoomDb() {
        serviceScope.launch {
            currentLocationUseCase.getCurrentLocation { locationData ->
                locationData?.let {
                    val currentTime = LocalDateTime.now()

                    val locationModel = LocationModel(
                        latitude = locationData.latitude,
                        longitude = locationData.longitude,
                        //timestamp = formatTimestamp(timestamp),
                        timestamp = localDateTimeToTimestamp(currentTime),
                        isSynchronized = false
                    )
                    serviceScope.launch {
                        locationTracker.saveGpsLocation(locationModel)
                    }
                }
            }
        }
    }

    private fun localDateTimeToTimestamp(localDateTime: LocalDateTime): Long {
        val instant = localDateTime.toInstant(ZoneOffset.UTC) // Convert to Instant
        return instant.toEpochMilli() // Extract milliseconds since epoch
    }

/*    private fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp)
        val dateFormat = SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }*/

    private fun isInternetConnected(): Boolean {
        val connectivityManager =
            application.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
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

