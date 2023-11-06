package com.example.gpstracker.roomdb

import com.example.gpstracker.roomdb.dao.LocationDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationDao: LocationDao
): LocationRepository {

    override suspend fun saveGpsLocation(location: LocationModel) {
        withContext(Dispatchers.IO) {
            locationDao.insertLocation(location)
        }
    }

    override suspend fun getUnsynchronizedLocations(): List<LocationModel> {
        return withContext(Dispatchers.IO){
            locationDao.getUnsynchronizedLocations()
        }
    }

    override suspend fun getAllLocations(): List<LocationModel> {
        return withContext(Dispatchers.IO){
            locationDao.getAllLocations()
        }
    }

    override suspend fun markLocationAsSynchronizedBasedOnId(id: Long) {
        withContext(Dispatchers.IO){
            locationDao.markAsSynchronized(id)
        }
    }
}