package com.example.gpstracker.repository

import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

abstract class LocationSaveRepository {

    abstract fun saveLocationToFirebase()

    abstract fun isLocationSavedFromRoomDatabase(latitude: Double, longitude: Double): Boolean
}