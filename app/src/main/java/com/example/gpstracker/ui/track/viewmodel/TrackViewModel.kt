package com.example.gpstracker.ui.track.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
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
    private val firebaseDatabase: DatabaseReference,
    private val application: Application,
    private val locationRepository: LocationRepository,
    private val locationTrackerUseCase: LocationTrackerUseCase,
    private val workManager: WorkManager,
    private val dataStorePreference: DataStorePreference
) : AndroidViewModel(application) {

    private var internetTimer: Timer? = null

    val _stateLiveData = MutableLiveData<TrackerState>()
    fun getStateLiveData(): LiveData<TrackerState> {
        return _stateLiveData
    }

/*    suspend fun getDataStoreUID(): String? {
        return viewModelScope.async {
            dataStorePreference.getData("UID")
        }.await()
    }*/


    init {
        _stateLiveData.value = TrackerState.OFF
    }


    private fun saveLocation() {
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

    fun startTrackingService() {
        val isInternetConnected = isInternetConnected()
        val isFirebaseConnected = isFirebaseDatabaseAvailable()

        if (isInternetConnected && isFirebaseConnected) {
            saveLocation()
        } else {
            saveToRoomDatabase()
        }
    }

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
            application.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

























