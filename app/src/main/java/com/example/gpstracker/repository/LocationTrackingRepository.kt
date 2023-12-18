package com.example.gpstracker.repository

import com.example.gpstracker.ui.track.LocationData

abstract class LocationTrackingRepository {

    abstract suspend fun getCurrentLocation(callback: (LocationData?) -> Unit)
}