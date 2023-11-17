package com.example.gpstracker.ui.track.viewmodel

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.gpstracker.prefs.DataStorePreference
import com.example.gpstracker.roomdb.LocationModel
import com.example.gpstracker.roomdb.LocationRepository
import com.example.gpstracker.ui.service.TrackingService
import com.example.gpstracker.ui.track.TrackerState
import com.example.gpstracker.usecase.FirebaseDatabaseUseCase
import com.example.gpstracker.usecase.LocationTrackerUseCase
import com.example.gpstracker.usecase.workManager.CustomWorker
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

class TrackViewModel @Inject constructor(
    private val locationServiceUseCase: FirebaseDatabaseUseCase,
    private val firebaseDatabase: DatabaseReference,
    private val applicationContext: Context,
    private val locationRepository: LocationRepository,
    private val locationTrackerUseCase: LocationTrackerUseCase,
    private val workManager: WorkManager,
    private val dataStorePreference: DataStorePreference
) : ViewModel() {


    private var internetTimer: Timer? = null
    private var dataSentTimer: Timer? = null

    val _stateLiveData = MutableLiveData<TrackerState>()
    fun getStateLiveData(): LiveData<TrackerState> {
        return _stateLiveData
    }

    private val locationManager =
        applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var lastLocation: Location? = null

    suspend fun getDataStoreUID(): String? {
        return viewModelScope.async {
            dataStorePreference.getData("UID")
        }.await()
    }


    init {
        _stateLiveData.value = TrackerState.OFF
    }


    private fun saveLocation() {
        Intent(applicationContext.applicationContext, TrackingService::class.java).also {
            it.action = TrackingService.Actions.START.toString()
            applicationContext.applicationContext.startForegroundService(it)
        }
    }

    fun stopTrackingService() {
        Intent(applicationContext.applicationContext, TrackingService::class.java).also {
            it.action = TrackingService.Actions.STOP.toString()
            applicationContext.applicationContext.stopService(it)
        }
    }



    /*    fun saveLocation() {
            viewModelScope.launch {
                locationServiceUseCase.saveLocation()
            }
        }*/


    /*    fun startTracking(){
            if (dataSentTimer == null) {
                dataSentTimer = Timer()
                val trackingIntervalMillis = com.example.gpstracker.BuildConfig.TRACKING_INTERVAL_MILLIS // 10000L//   10000L// 10 sec
                dataSentTimer?.scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        val isInternetConnected = isInternetConnected()
                        val isFirebaseConnected = isFirebaseDatabaseAvailable()
                        if (isInternetConnected == true && isFirebaseConnected == true) {
                            saveLocation()
                        } else {
                            saveToRoomDatabase()
                        }
                    }
                }, 0, trackingIntervalMillis)
            }
        }*/

    fun startTrackingService() {
        //val trackingIntervalMillis = 10 * 60 * 1000L // 10 minutes (10 * 60 seconds * 1000 milliseconds)
        // val minDistance = 60.0f // Sensitivity: 60 meters

        //val locationListener = LocationListener { location ->
        val isInternetConnected = isInternetConnected()
        val isFirebaseConnected = isFirebaseDatabaseAvailable()

        if (isInternetConnected && isFirebaseConnected) {
            saveLocation()
        } else {
            saveToRoomDatabase()
        }

//            val currentLocation = lastLocation
//            if (currentLocation == null || location.distanceTo(currentLocation) >= minDistance) {
//                lastLocation = location
//            }
        //  }

    }

    /*    fun startTracking() {
            val trackingIntervalMillis = 10 * 60 * 1000L // 10 minutes (10 * 60 seconds * 1000 milliseconds)
            val minDistance = 60.0f // Sensitivity: 60 meters

            val locationListener = LocationListener { location ->
                val isInternetConnected = isInternetConnected()
                val isFirebaseConnected = isFirebaseDatabaseAvailable()

                if (isInternetConnected && isFirebaseConnected) {
                    saveLocation()
                } else {
                    saveToRoomDatabase()
                }

                val currentLocation = lastLocation
                if (currentLocation == null || location.distanceTo(currentLocation) >= minDistance) {
                    lastLocation = location
                }
            }

            try {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    trackingIntervalMillis,
                    minDistance,
                    locationListener
                )
            } catch (e: SecurityException) {
                // Handle location permission denied
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
                    val isFirebaseConnected = isFirebaseDatabaseAvailable()
                    val currentState = getStateLiveData().value
                    if (currentState == TrackerState.DISCONNECTED && isInternetConnected == true && isFirebaseConnected == true) {
                        _stateLiveData.postValue(TrackerState.ON)
                    } else if (currentState == TrackerState.ON && (isInternetConnected == false || isFirebaseConnected == false)) {
                        _stateLiveData.postValue(TrackerState.DISCONNECTED)
                    }
                }
            }, 0, trackingIntervalMillis)
        }
    }

    private fun isInternetConnected(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


    private fun isFirebaseDatabaseAvailable(): Boolean {
        return firebaseDatabase != null
    }

    private fun saveToRoomDatabase() {
        locationTrackerUseCase.getCurrentLocation { locationData ->
            if (locationData != null) {
                val timestamp = System.currentTimeMillis()
                val locationModel = LocationModel(
                    latitude = locationData.latitude,
                    longitude = locationData.longitude,
                    timestamp = formatTimestamp(timestamp),
                    isSynchronized = false
                )

                viewModelScope.launch {
                    locationRepository.saveGpsLocation(locationModel)
                }
            } else {
                // Handle the case where location data is not available
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

























