package com.example.gpstracker.roomdb

import androidx.room.Insert

interface LocationRepository {

    @Insert
    suspend fun saveGpsLocation(location: LocationModel)

   // suspend fun getGpsLocation(location: LocationModel)

}