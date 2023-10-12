package com.example.gpstracker.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gpstracker.roomdb.LocationModel

@Dao
interface LocationDao {

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    fun insertLocation(location: LocationModel)
}