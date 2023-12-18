package com.example.gpstracker.usecase

import com.example.gpstracker.network.RequestResult
import com.example.gpstracker.repository.AuthenticationRepository
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val firebaseAuthenticationRepository: AuthenticationRepository
) {

    suspend fun loginUser(email: String, password: String): RequestResult<AuthResult> {
        return firebaseAuthenticationRepository.loginUser(email, password)
    }
}