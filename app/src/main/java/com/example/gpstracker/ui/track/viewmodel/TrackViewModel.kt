package com.example.gpstracker.ui.track.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gpstracker.roomdb.LocationModel
import com.example.gpstracker.roomdb.LocationRepository
import com.example.gpstracker.usecase.LocationServiceUseCase
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class TrackViewModel @Inject constructor(
    private val locationServiceUseCase: LocationServiceUseCase,
    private val firebaseDatabase: DatabaseReference,
    private val applicationContext: Context,
    private val locationRepository: LocationRepository
) : ViewModel() {

    fun startTracking() {
        viewModelScope.launch {
            locationServiceUseCase.startLocationUpdates()
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

    /*    fun saveToRoomDatabase() {
            locationServiceUseCase.getCurrentLocation { locationData ->
                if (locationData != null) {
                    viewModelScope.launch {
                        roomDatabase.saveLocation(locationData)
                    }
                } else {
                    // Handle the case where location data is not available
                }
            }
        }*/

    fun saveToRoomDatabase() {
        locationServiceUseCase.getCurrentLocation { locationData ->
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

}