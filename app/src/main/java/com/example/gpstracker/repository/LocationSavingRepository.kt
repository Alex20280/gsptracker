package com.example.gpstracker.repository

abstract class LocationSavingRepository {

    abstract suspend fun saveLocationToFirebase()

    abstract suspend fun isLocationSavedFromRoomDatabase(latitude: Double, longitude: Double): Boolean
}