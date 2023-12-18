package com.example.gpstracker.usecase

import com.example.gpstracker.network.RequestResult
import com.example.gpstracker.repository.AuthenticationRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val firebaseAuthenticationRepository: AuthenticationRepository
) {
    suspend fun registerUser(email: String, password: String): RequestResult<Task<AuthResult>> {
        return firebaseAuthenticationRepository.registerUser(email, password)
    }

}
