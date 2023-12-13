package com.example.gpstracker.di

import android.content.Context
import com.example.gpstracker.repository.FirebaseAuthenticationRepository
import com.example.gpstracker.repository.FirebaseAuthenticationRepositoryImpl
import com.example.gpstracker.repository.LocationTrackerRepository
import com.example.gpstracker.repository.LocationTrackerRepositoryImpl
import com.example.gpstracker.usecase.FirebaseDatabaseUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object LocationServiceModule {

    @Provides
    fun provideLocationServiceUseCase(fusedLocationClient: FusedLocationProviderClient, database: DatabaseReference): FirebaseDatabaseUseCase {
        return FirebaseDatabaseUseCase(fusedLocationClient, database)
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

    @Provides
    @Singleton
    fun provideLocationTrackerRepository(impl: LocationTrackerRepositoryImpl): LocationTrackerRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthenticationRepository(impl: FirebaseAuthenticationRepositoryImpl): FirebaseAuthenticationRepository {
        return impl
    }

}