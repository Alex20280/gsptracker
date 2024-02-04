package com.example.gpstracker.di

import android.content.Context
import com.example.gpstracker.repository.AuthenticationRepository
import com.example.gpstracker.repository.AuthenticationRepositoryImpl
import com.example.gpstracker.repository.LocationSavingRepository
import com.example.gpstracker.repository.LocationSavingRepositoryImpl
import com.example.gpstracker.repository.LocationTrackingRepository
import com.example.gpstracker.repository.LocationTrackingRepositoryImpl
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
    fun provideLocationServiceUseCase(locationSaveRepositoryImpl: LocationSavingRepositoryImpl): FirebaseDatabaseUseCase {
        return FirebaseDatabaseUseCase(locationSaveRepositoryImpl)
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
    fun provideLocationTrackerRepository(impl: LocationTrackingRepositoryImpl): LocationTrackingRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthenticationRepository(impl: AuthenticationRepositoryImpl): AuthenticationRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideLocationSaveRepository(impl: LocationSavingRepositoryImpl): LocationSavingRepository {
        return impl
    }

}