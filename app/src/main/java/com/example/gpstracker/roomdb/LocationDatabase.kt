package com.example.gpstracker.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gpstracker.roomdb.LocationDatabase.Companion.VERSION_DATABASE
import com.example.gpstracker.roomdb.dao.LocationDao

@Database(entities =[LocationModel::class], version = VERSION_DATABASE )
abstract class LocationDatabase : RoomDatabase() {

    abstract val locationDao: LocationDao

    companion object {
        const val VERSION_DATABASE = 2
        const val DATABASE_NAME = "gps_location_db"
    }
}