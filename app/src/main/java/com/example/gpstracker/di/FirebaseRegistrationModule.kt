package com.example.gpstracker.di

import android.app.Application
import com.example.gpstracker.usecase.FirebaseAuthenticationUseCase
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object FirebaseRegistrationModule {

    @Provides
    fun provideFirebaseRegistrationUseCase(auth: FirebaseAuth): FirebaseAuthenticationUseCase {
        return FirebaseAuthenticationUseCase(auth)
    }

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}