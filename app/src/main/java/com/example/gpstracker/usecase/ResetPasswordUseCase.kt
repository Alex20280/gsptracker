package com.example.gpstracker.usecase

import com.example.gpstracker.network.RequestResult
import com.example.gpstracker.repository.AuthenticationRepository
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val firebaseAuthenticationRepository: AuthenticationRepository
) {

    suspend fun resetPassword(email: String): Task<RequestResult<Unit>> {
        return firebaseAuthenticationRepository.resetPassword(email)
    }
}