package com.example.gpstracker.ui.track.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.gpstracker.roomdb.LocationRepository
import com.example.gpstracker.ui.service.TrackingService
import com.example.gpstracker.ui.track.TrackerState
import com.example.gpstracker.usecase.workManager.CustomWorker
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

class TrackViewModel @Inject constructor(
    private val application: Application,
    private val workManager: WorkManager
) : AndroidViewModel(application) {

    private var gpsTimer: Timer? = null
    private var internetTimer: Timer? = null
    private var internetOfflineSyncTriggered = false

    val _stateLiveData = MutableLiveData<TrackerState>()
    fun getStateLiveData(): LiveData<TrackerState> {
        return _stateLiveData
    }

    init {
        _stateLiveData.value = TrackerState.OFF
    }

    fun startTrackingService() {
        Intent(application.applicationContext, TrackingService::class.java).also {
            it.action = TrackingService.Actions.START.toString()
            application.applicationContext.startForegroundService(it)
        }
    }

    fun stopTrackingService() {
        Intent(application.applicationContext, TrackingService::class.java).also {
            it.action = TrackingService.Actions.STOP.toString()
            application.applicationContext.stopService(it)
        }
    }

    fun stopTrackGpsAvailabilityCheck() {
        gpsTimer?.cancel()
        gpsTimer = null
    }

    fun stopInternetAvailabilityCheck() {
        internetTimer?.cancel()
        internetTimer = null
    }

    fun startTrackGpsAvailability() {
        if (gpsTimer == null) {
            gpsTimer = Timer()
            val trackingIntervalMillis = 5000L // 5 sec
            gpsTimer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    // val isInternetConnected = isInternetConnected()
                    val isLocationEnabled = isLocationEnabled()
                    val currentState = getStateLiveData().value
                    //if (currentState == TrackerState.DISCONNECTED && isInternetConnected == true && isLocationEnabled == true) {
                    if (currentState == TrackerState.DISCONNECTED && isLocationEnabled == true) {
                        _stateLiveData.postValue(TrackerState.OFF)
                        //   } else if (currentState == TrackerState.ON && (isInternetConnected == false|| isLocationEnabled == false)) {
                    } else if (currentState == TrackerState.ON && isLocationEnabled == false) {
                        _stateLiveData.postValue(TrackerState.DISCONNECTED)
                    }
                }
            }, 0, trackingIntervalMillis)
        }
    }

    fun isInternetConnected(): Boolean {
        val connectivityManager =
            application.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun isLocationEnabled(): Boolean {
        val locationManager =
            application.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun requestLocationEnable() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        application.applicationContext.startActivity(intent)
    }

    fun syncLocalDatabaseAndRemoteDatabase() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val syncDataRequest = OneTimeWorkRequest.Builder(CustomWorker::class.java)
            .setConstraints(constraints)
            .build()

        workManager.beginUniqueWork("Worker", ExistingWorkPolicy.REPLACE, syncDataRequest).enqueue()
    }

    fun startTrackInternetAvailability() {
        if (internetTimer == null) {
            internetTimer = Timer()
            val trackingIntervalMillis = 10000L
            internetTimer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    val isInternetAvailable = isInternetConnected()
                    if (!isInternetAvailable && !internetOfflineSyncTriggered) {
                        internetOfflineSyncTriggered = true
                        syncLocalDatabaseAndRemoteDatabase() // Start workmanager
                    }
                    else if (isInternetAvailable) {
                        internetOfflineSyncTriggered = false
                    }
                }
            }, 0, trackingIntervalMillis)
        }
    }

}

























