package com.example.gpstracker.repository

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class LocationSavingRepositoryImpl @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val database: DatabaseReference
) : LocationSavingRepository() {

    @SuppressLint("MissingPermission")
    override suspend fun saveLocationToFirebase() {
        val locationRequest = LocationRequest().apply {
            interval = com.example.gpstracker.BuildConfig.TRACKING_INTERVAL_MILLIS //10 sec
            fastestInterval = com.example.gpstracker.BuildConfig.TRACKING_INTERVAL_MILLIS //10 sec
            smallestDisplacement =
                com.example.gpstracker.BuildConfig.TRACKING_INTERVAL_METERS.toFloat() //60 meters
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.d("LocationUpdate", "Got location result: $locationResult")
                locationResult.locations.let { locations ->
                    val location = locations.last()
                    saveLocationFromRoomDbToFirebase(location.latitude, location.longitude)
                }
            }

        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    override suspend fun isLocationSavedFromRoomDatabase(
        latitude: Double,
        longitude: Double,
        time: Long
    ): Boolean {
        saveLocationFromRoomDbToFirebase(latitude, longitude, time)
        return true
    }

    private fun saveLocationFromRoomDbToFirebase(
        latitude: Double,
        longitude: Double,
        time: Any = LocalDateTime.now()
    ) {
        val currentTime = when (time) {
            is Long -> time
            is LocalDateTime -> localDateTimeToTimestamp(time)
            else -> localDateTimeToTimestamp(LocalDateTime.now())
        }

        // Reference the "locations" node with the user's ID
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val userRef = database.child(userId!!)
        val locationRef = userRef.child(currentTime.toString())

        val location = hashMapOf<String, Any>(
            "lat" to latitude,
            "long" to longitude
        )

        locationRef.setValue(location)
    }

    private fun localDateTimeToTimestamp(localDateTime: LocalDateTime): Long {
        val instant = localDateTime.toInstant(ZoneOffset.UTC) // Convert to Instant
        return instant.toEpochMilli() // Extract milliseconds since epoch
    }

}


