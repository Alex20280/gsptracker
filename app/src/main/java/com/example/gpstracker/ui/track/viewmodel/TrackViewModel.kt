package com.example.gpstracker.ui.track.viewmodel

import android.content.Context
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
import com.example.gpstracker.prefs.UserPreferences
import com.example.gpstracker.roomdb.LocationModel
import com.example.gpstracker.roomdb.LocationRepository
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

    val _stateLiveData = MutableLiveData<TrackerState>()
    fun getStateLiveData(): LiveData<TrackerState> {
        return _stateLiveData
    }

    suspend fun getDataStoreUID(): String? {
        return viewModelScope.async {
            dataStorePreference.getData("UID")
        }.await()
    }


    init {
        _stateLiveData.value = TrackerState.OFF
    }

    fun saveLocation() {
        viewModelScope.launch {
            locationServiceUseCase.saveLocation()
        }
    }

    fun isInternetConnected(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


    fun isFirebaseDatabaseAvailable(): Boolean {
        return firebaseDatabase != null
    }

    fun saveToRoomDatabase() {
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

























