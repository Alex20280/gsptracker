package com.example.gpstracker.usecase

import com.example.gpstracker.repository.LocationTrackerRepository
import com.example.gpstracker.ui.track.LocationData
import javax.inject.Inject

class CurrentLocationUseCase @Inject constructor(
    private val locationRepository: LocationTrackerRepository
) {
    fun getCurrentLocation(callback: (LocationData?) -> Unit) {
        locationRepository.getCurrentLocation(callback)
    }
}