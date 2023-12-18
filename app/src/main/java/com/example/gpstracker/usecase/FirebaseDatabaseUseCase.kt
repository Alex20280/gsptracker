package com.example.gpstracker.usecase

import com.example.gpstracker.repository.LocationSavingRepository
import javax.inject.Inject

class FirebaseDatabaseUseCase @Inject constructor(
    private val locationSaveRepository: LocationSavingRepository
) {
    suspend fun saveLocationToFirebase(){
        locationSaveRepository.saveLocationToFirebase()
    }

    suspend fun isLocationSavedFromRoomDatabase(latitude: Double, longitude: Double): Boolean {
        return locationSaveRepository.isLocationSavedFromRoomDatabase(latitude, longitude)
    }
}
