package com.example.gpstracker.usecase

import android.util.Log
import androidx.annotation.NonNull
import com.example.gpstracker.network.ErrorDto
import com.example.gpstracker.network.RequestResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await


class FirebaseAuthenticationUseCase(private val auth: FirebaseAuth) {

    fun registerUser(email: String, password: String): RequestResult<Task<AuthResult>> {
        try {
            val authResultTask = auth.createUserWithEmailAndPassword(email, password)
            return RequestResult.Success(authResultTask)
        } catch (e: Exception) {
            // Handle exceptions here
            return RequestResult.Error(ErrorDto.Default("Registration problem"), 0)
        }
    }

    suspend fun loginUser(email: String, password: String): RequestResult<AuthResult> {
        return try {
            val authResultTask = auth.signInWithEmailAndPassword(email, password)
            authResultTask.await() // Wait for the task to complete
            // If you reach this point, the task is considered successful
            RequestResult.Success(authResultTask.result!!)
        } catch (e: Exception) {
            // Handle exceptions and errors here
            RequestResult.Error(ErrorDto.Default(e.localizedMessage ?: "Login problem"), 0)
        }
    }

    fun resetPassword(email: String): Task<RequestResult<Unit>> {
        val resetResultTask = auth.sendPasswordResetEmail(email)

        return resetResultTask.continueWith { task ->
            if (task.isSuccessful) {
                RequestResult.Success(Unit)
            } else {
                RequestResult.Error(ErrorDto.Default("Reset problem"), 0)
            }
        }
    }

    fun getUserUd(): String? {
        return auth.uid
    }

}
