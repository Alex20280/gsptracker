package com.example.gpstracker.usecase

import com.example.gpstracker.repository.LocationSavingRepository
import java.time.LocalDateTime
import javax.inject.Inject

class FirebaseDatabaseUseCase @Inject constructor(
    private val locationSaveRepository: LocationSavingRepository
) {
    suspend fun saveLocationToFirebase(){
        locationSaveRepository.saveLocationToFirebase()
    }

    suspend fun isLocationSavedFromRoomDatabase(latitude: Double, longitude: Double, time: Long): Boolean {
        return locationSaveRepository.isLocationSavedFromRoomDatabase(latitude, longitude, time)
    }
}
