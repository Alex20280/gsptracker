package com.example.gpstracker.usecase

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import com.example.gpstracker.repository.LocationSaveRepository
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
    private val locationSaveRepository: LocationSaveRepository
) {
    fun saveLocationToFirebase(){
        locationSaveRepository.saveLocationToFirebase()
    }

    fun isLocationSavedFromRoomDatabase(latitude: Double, longitude: Double): Boolean {
        return locationSaveRepository.isLocationSavedFromRoomDatabase(latitude, longitude)
    }
}
