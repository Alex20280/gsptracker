package com.example.gpstracker.usecase

import com.example.gpstracker.repository.LocationTrackingRepository
import com.example.gpstracker.ui.track.LocationData
import javax.inject.Inject

class ReceiveCurrentLocationUseCase @Inject constructor(
    private val locationRepository: LocationTrackingRepository
) {
    suspend fun getCurrentLocation(callback: (LocationData?) -> Unit) {
        locationRepository.getCurrentLocation(callback)
    }
}