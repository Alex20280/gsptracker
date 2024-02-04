package com.example.gpstracker.di

import android.content.Context
import androidx.room.Room
import com.example.gpstracker.roomdb.LocationDatabase
import com.example.gpstracker.roomdb.LocationRepository
import com.example.gpstracker.roomdb.LocationRepositoryImpl
import com.example.gpstracker.roomdb.dao.LocationDao
import dagger.Module
import dagger.Provides


@Module
object RoomDatabaseModule {

    @Provides
    fun provideAppDatabase(context: Context): LocationDatabase {
        return Room.databaseBuilder(context, LocationDatabase::class.java, "gps_location_db").fallbackToDestructiveMigration().build()
    }

    @Provides
    fun providePackageDao(appDatabase: LocationDatabase): LocationDao {
        return appDatabase.locationDao
    }

    @Provides
    fun getUserPlanRepository(locationDao: LocationDao): LocationRepository = LocationRepositoryImpl(locationDao)

}
