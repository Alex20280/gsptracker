package com.example.gpstracker.di

import android.content.Context
import com.example.gpstracker.usecase.LocationServiceUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocationServiceModule {

    @Provides
    fun provideLocationServiceUseCase(fusedLocationClient: FusedLocationProviderClient, database: DatabaseReference): LocationServiceUseCase {
        return LocationServiceUseCase(fusedLocationClient, database)
    }

    @Provides
    fun provideFusedLocationProviderClient(context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child("locations")
    }


}