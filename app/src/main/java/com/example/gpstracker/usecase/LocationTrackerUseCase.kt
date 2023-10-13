package com.example.gpstracker.usecase

import android.annotation.SuppressLint
import android.location.Location
import com.example.gpstracker.ui.track.LocationData
import com.google.android.gms.location.FusedLocationProviderClient

class LocationTrackerUseCase(
    private val fusedLocationClient: FusedLocationProviderClient
){

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(callback: (LocationData?) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val locationData = LocationData(location.latitude, location.longitude)
                    callback(locationData)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                callback(null)
            }
    }
}