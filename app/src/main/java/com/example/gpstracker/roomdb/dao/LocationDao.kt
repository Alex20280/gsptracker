package com.example.gpstracker.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gpstracker.roomdb.LocationModel

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLocation(location: LocationModel)

    @Query("SELECT * FROM locations WHERE isSynchronized = 0")
    fun getUnsynchronizedLocations(): List<LocationModel>

    @Query("SELECT * FROM locations")
    fun getAllLocations(): List<LocationModel>

    @Query("UPDATE locations SET isSynchronized = 1 WHERE id = :id")
    fun markAsSynchronized(id: Long)
}