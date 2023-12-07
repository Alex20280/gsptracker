package com.example.gpstracker.repository

import android.annotation.SuppressLint
import android.location.Location
import com.example.gpstracker.ui.track.LocationData
import com.google.android.gms.location.FusedLocationProviderClient
import javax.inject.Inject

class LocationTrackerRepository @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) {

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(callback: (LocationData?) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val locationData = LocationData(location.latitude, location.longitude)
                    callback(locationData)
                } ?: run {
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                callback(null)
            }
    }
}