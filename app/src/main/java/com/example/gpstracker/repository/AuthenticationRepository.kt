package com.example.gpstracker.repository

import com.example.gpstracker.network.RequestResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

abstract class AuthenticationRepository {

    abstract suspend fun registerUser(email: String, password: String): RequestResult<Task<AuthResult>>

    abstract suspend fun loginUser(email: String, password: String): RequestResult<AuthResult>

    abstract suspend fun resetPassword(email: String): Task<RequestResult<Unit>>

    abstract suspend fun getUserUd() : String?
}