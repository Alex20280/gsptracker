package com.example.gpstracker.usecase

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.database.DatabaseReference
import com.example.gpstracker.ui.track.LocationData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseDatabaseUseCase(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val database: DatabaseReference
) {

    @SuppressLint("MissingPermission")
    fun saveLocation() {
        // Start location tracking
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    saveLocationToFirebase(location.latitude, location.longitude)
                  }
            }
    }

    fun saveLocationFromRoomDatabase(latitude: Double, longitude: Double) : Boolean {
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
        val locationData = LocationData(latitude, longitude)
        locationEntry.setValue(locationData)
    }

/*    @SuppressLint("MissingPermission")
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
}