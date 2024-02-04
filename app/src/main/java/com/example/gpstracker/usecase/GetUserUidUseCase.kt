package com.example.gpstracker.usecase

import com.example.gpstracker.repository.AuthenticationRepository
import javax.inject.Inject

class GetUserUidUseCase @Inject constructor(
    private val firebaseAuthenticationRepository: AuthenticationRepository
){

    suspend fun getUserUid(): String? {
        return firebaseAuthenticationRepository.getUserUd()
    }
}