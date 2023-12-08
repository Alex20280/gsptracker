package com.example.gpstracker.ui.track.viewmodel

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.gpstracker.prefs.DataStorePreference
import com.example.gpstracker.repository.LocationTrackerRepository
import com.example.gpstracker.roomdb.LocationModel
import com.example.gpstracker.roomdb.LocationRepository
import com.example.gpstracker.ui.service.TrackingService
import com.example.gpstracker.ui.track.TrackerState
import com.example.gpstracker.usecase.workManager.CustomWorker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

class TrackViewModel @Inject constructor(
    private val application: Application,
    private val locationRepository: LocationTrackerRepository,
    private val locationTracker: LocationRepository,
    private val workManager: WorkManager
) : AndroidViewModel(application) {

    private var internetTimer: Timer? = null

    val _stateLiveData = MutableLiveData<TrackerState>()
    fun getStateLiveData(): LiveData<TrackerState> {
        return _stateLiveData
    }

    init {
        _stateLiveData.value = TrackerState.OFF
    }

     fun saveLocation() {
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

/*    fun startTrackingService() {
        val isInternetConnected = isInternetConnected()
        val isGpsEnabled = isLocationEnabled()

        if(!isGpsEnabled) {

            AlertDialog.Builder(application.applicationContext)
                .setTitle("GPS Disabled")
                .setMessage("Please enable location services to allow tracking")
                .setPositiveButton("Enable") { _, _ ->
                    requestLocationEnable()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()

            return
        }
        if (isInternetConnected) {
            saveLocation()
        } else {
            saveToRoomDatabase()
        }
    }*/

    fun stopTrackInternetAvailability() {
        internetTimer?.cancel()
        internetTimer = null
    }

    fun startTrackInternetAvailability() {
        if (internetTimer == null) {
            internetTimer = Timer()
            val trackingIntervalMillis = 5000L//600000L  // 5 minutes
            internetTimer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    val isInternetConnected = isInternetConnected()
                    val isLocationEnabled = isLocationEnabled()
                    val currentState = getStateLiveData().value
                    if (currentState == TrackerState.DISCONNECTED && isInternetConnected == true && isLocationEnabled == true) {
                        _stateLiveData.postValue(TrackerState.ON)
                    } else if (currentState == TrackerState.ON && (isInternetConnected == false|| isLocationEnabled == false)) {
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
        val locationManager = application.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun requestLocationEnable() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        application.applicationContext.startActivity(intent)
    }



    /*    private fun isFirebaseDatabaseAvailable(): Boolean {
            return firebaseDatabase != null
        }*/

    fun saveToRoomDatabase() {
        locationRepository.getCurrentLocation { locationData ->
            locationData?.let {
                val timestamp = System.currentTimeMillis()
                val locationModel = LocationModel(
                    latitude = locationData.latitude,
                    longitude = locationData.longitude,
                    timestamp = formatTimestamp(timestamp),
                    isSynchronized = false
                )
                viewModelScope.launch {
                    locationTracker.saveGpsLocation(locationModel)
                }
            }
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp)
        val dateFormat = SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault())
        return dateFormat.format(date)
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

}

























