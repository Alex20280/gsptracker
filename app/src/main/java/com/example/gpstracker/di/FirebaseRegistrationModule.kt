package com.example.gpstracker.di

import com.example.gpstracker.repository.AuthenticationRepositoryImpl
import com.example.gpstracker.usecase.GetUserUidUseCase
import com.example.gpstracker.usecase.LoginUserUseCase
import com.example.gpstracker.usecase.RegisterUserUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
object FirebaseRegistrationModule {

    @Provides
    fun provideFirebaseRegistrationUseCase(firebaseAuthenticationRepository: AuthenticationRepositoryImpl): RegisterUserUseCase {
        return RegisterUserUseCase(firebaseAuthenticationRepository)
    }

    @Provides
    fun provideLoginUserUseCase(firebaseAuthenticationRepository: AuthenticationRepositoryImpl): LoginUserUseCase {
        return LoginUserUseCase(firebaseAuthenticationRepository)
    }

    @Provides
    fun provideGetUserUidUseCase(firebaseAuthenticationRepository: AuthenticationRepositoryImpl): GetUserUidUseCase {
        return GetUserUidUseCase(firebaseAuthenticationRepository)
    }

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}

