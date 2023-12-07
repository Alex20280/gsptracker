package com.example.gpstracker.usecase

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.database.DatabaseReference
import com.google.android.gms.location.LocationRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ServerValue
import com.google.firebase.ktx.Firebase
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult
import javax.inject.Inject

class FirebaseDatabaseUseCase @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val database: DatabaseReference
) {

    @SuppressLint("MissingPermission")
    fun saveLocation() {
        val locationRequest = LocationRequest().apply {
            interval = com.example.gpstracker.BuildConfig.TRACKING_INTERVAL_MILLIS
            fastestInterval = com.example.gpstracker.BuildConfig.TRACKING_INTERVAL_MILLIS
            smallestDisplacement = com.example.gpstracker.BuildConfig.TRACKING_INTERVAL_METERS.toFloat()
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.let { locations ->
                    val location = locations.last()
                    saveLocationToFirebase(location.latitude, location.longitude)
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }


    fun saveLocationFromRoomDatabase(latitude: Double, longitude: Double): Boolean {
        saveLocationToFirebase(latitude, longitude)
        return true
    }

    private fun saveLocationToFirebase(latitude: Double, longitude: Double) {
        // Reference the "locations" node with the user's ID
        val userId = Firebase.auth.currentUser
        val userLocationsRef = database.child(userId?.uid.toString())

        // Create a new location entry with a unique key
        val locationEntry = userLocationsRef.push()

        // Set the latitude and longitude data for this location entry
        //val locationData = LocationData(latitude, longitude)

        val locationData = HashMap<String, Any>()
        locationData["latitude"] = latitude
        locationData["longitude"] = longitude
        locationData["timestamp"] = ServerValue.TIMESTAMP
        locationEntry.setValue(locationData)

        Log.d(
            "myLoccation",
            userId.toString() + latitude.toString() + "longitude" + longitude.toString() + "timestamp" + ServerValue.TIMESTAMP
        )
    }
}
