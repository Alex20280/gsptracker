package com.example.gpstracker.repository

import java.time.LocalDateTime

abstract class LocationSavingRepository {

    abstract suspend fun saveLocationToFirebase()

    abstract suspend fun isLocationSavedFromRoomDatabase(latitude: Double, longitude: Double, time: Long): Boolean
}