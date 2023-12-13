package com.example.gpstracker.repository

import com.example.gpstracker.ui.track.LocationData

abstract class LocationTrackerRepository {

    abstract fun getCurrentLocation(callback: (LocationData?) -> Unit)
}