package com.example.gpstracker.repository

import com.example.gpstracker.network.ErrorDto
import com.example.gpstracker.network.RequestResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await

abstract class FirebaseAuthenticationRepository {

    abstract fun registerUser(email: String, password: String): RequestResult<Task<AuthResult>>

    abstract suspend fun loginUser(email: String, password: String): RequestResult<AuthResult>

    abstract fun resetPassword(email: String): Task<RequestResult<Unit>>

    abstract fun getUserUd() : String?
}