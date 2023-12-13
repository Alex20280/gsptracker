package com.example.gpstracker.di

import com.example.gpstracker.repository.FirebaseAuthenticationRepository
import com.example.gpstracker.repository.FirebaseAuthenticationRepositoryImpl
import com.example.gpstracker.usecase.FirebaseAuthenticationUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
object FirebaseRegistrationModule {

    @Provides
    fun provideFirebaseRegistrationUseCase(firebaseAuthenticationRepository: FirebaseAuthenticationRepositoryImpl): FirebaseAuthenticationUseCase {
        return FirebaseAuthenticationUseCase(firebaseAuthenticationRepository)
    }

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}