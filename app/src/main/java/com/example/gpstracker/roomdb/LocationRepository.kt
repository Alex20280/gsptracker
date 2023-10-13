package com.example.gpstracker.roomdb

import androidx.room.Insert
import androidx.room.Query

interface LocationRepository {

    suspend fun saveGpsLocation(location: LocationModel)

    suspend fun getUnsynchronizedLocations(): List<LocationModel>

    suspend fun markLocationAsSynchronizedBasedOnId(id: Long)

}