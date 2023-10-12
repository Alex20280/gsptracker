package com.example.gpstracker.roomdb

import com.example.gpstracker.roomdb.dao.LocationDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationDao: LocationDao
): LocationRepository {

    override suspend fun saveGpsLocation(location: LocationModel) {
        withContext(Dispatchers.IO) {
            locationDao.insertLocation(location)
        }
    }
        //locationDao.insertLocation(location)

/*
    override suspend fun getGpsLocation(location: LocationModel) {
        locationDao.getAllLocations()
    }*/
}


/*
fun saveLocation(locationData: LocationData) {
    viewModelScope.launch {
        val timestamp = System.currentTimeMillis()
        val locationEntity = LocationModel(
            latitude = locationData.latitude,
            longitude = locationData.longitude,
            timestamp = formatTimestamp(timestamp),
            isSynchronized = false
        )
        locationRepository.saveGpsLocation(locationEntity)
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val dateFormat = SimpleDateFormat("HH:mm dd MMM yyyy", Locale.getDefault())
    return dateFormat.format(date)
}*/
