package com.example.gpstracker.di

import com.example.gpstracker.usecase.FirebaseAuthenticationUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
class FirebaseRegistrationModule {
    @Provides
    fun provideFirebaseRegistrationUseCase(auth: FirebaseAuth): FirebaseAuthenticationUseCase {
        return FirebaseAuthenticationUseCase(auth)
    }

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}