package com.example.gpstracker.usecase

import com.example.gpstracker.network.ErrorDto
import com.example.gpstracker.network.RequestResult
import com.example.gpstracker.repository.FirebaseAuthenticationRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthenticationUseCase @Inject constructor(
    private val firebaseAuthenticationRepository: FirebaseAuthenticationRepository
) {
    fun registerUser(email: String, password: String): RequestResult<Task<AuthResult>> {
        return firebaseAuthenticationRepository.registerUser(email, password)
    }

    suspend fun loginUser(email: String, password: String): RequestResult<AuthResult> {
        return firebaseAuthenticationRepository.loginUser(email, password)
    }

    fun resetPassword(email: String): Task<RequestResult<Unit>> {
        return firebaseAuthenticationRepository.resetPassword(email)
    }

    fun getUserUd(): String? {
        return firebaseAuthenticationRepository.getUserUd()
    }
}
