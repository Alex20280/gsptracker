package com.example.gpstracker.usecase

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.database.DatabaseReference
import com.example.gpstracker.ui.track.LocationData
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ServerValue
import com.google.firebase.ktx.Firebase
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

class FirebaseDatabaseUseCase(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val database: DatabaseReference,
    //private  val context: Context
) {

    @SuppressLint("MissingPermission")
    fun saveLocation() {

        val locationRequest = LocationRequest().apply {
            interval = 10000 // 10 seconds
            fastestInterval = 5000 // 5 seconds
            smallestDisplacement = 2F// 100 meters
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.let { locations ->
                    val location = locations.last()
                    saveLocationToFirebase(location.latitude, location.longitude)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

        // Get initial location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if(location != null) {
                    saveLocationToFirebase(location.latitude, location.longitude)
                }
            }
    }



/*    @SuppressLint("MissingPermission")
    fun saveLocation() {
        // Start location tracking
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    saveLocationToFirebase(location.latitude, location.longitude)
                }
            }
    }*/

    fun saveLocationFromRoomDatabase(latitude: Double, longitude: Double, ): Boolean {
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
/*   @SuppressLint("MissingPermission")
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
    }*/
