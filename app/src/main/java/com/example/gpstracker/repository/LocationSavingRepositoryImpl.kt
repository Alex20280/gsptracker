package com.example.gpstracker.repository

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class LocationSavingRepositoryImpl @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val database: DatabaseReference
): LocationSavingRepository() {

    @SuppressLint("MissingPermission")
    override suspend fun saveLocationToFirebase() {
        Log.d("LocationUpdate", "executed")
        val locationRequest = LocationRequest().apply {
            interval = com.example.gpstracker.BuildConfig.TRACKING_INTERVAL_MILLIS //10 sec
            fastestInterval = com.example.gpstracker.BuildConfig.TRACKING_INTERVAL_MILLIS //10 sec
            smallestDisplacement = com.example.gpstracker.BuildConfig.TRACKING_INTERVAL_METERS.toFloat() //60 meters
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.d("LocationUpdate", "Got location result: $locationResult")
                locationResult.locations.let { locations ->
                    val location = locations.last()
                    saveLocationToFirebase(location.latitude, location.longitude)
                }
            }

        }
        val result = fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
        //TODO: remove
        result.addOnFailureListener {
            Log.e("MyLocation", "Failed to request updates", it)
        }

        //TODO: remove
        result.addOnSuccessListener {
            Log.i("MyLocation", "Successfully requested updates")
        }
    }

    override suspend fun isLocationSavedFromRoomDatabase(latitude: Double, longitude: Double): Boolean {
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


