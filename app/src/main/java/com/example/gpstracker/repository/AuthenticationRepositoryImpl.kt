package com.example.gpstracker.repository

import com.example.gpstracker.network.ErrorResponse
import com.example.gpstracker.network.RequestResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthenticationRepository() {

    override suspend fun registerUser(email: String, password: String): RequestResult<Task<AuthResult>> {
        return try {
            val authResultTask = auth.createUserWithEmailAndPassword(email, password)
            RequestResult.Success(authResultTask)
        } catch (e: Exception) {
            RequestResult.Error(ErrorResponse.Default("Registration problem"), 0)
        }
    }

    override suspend fun loginUser(email: String, password: String): RequestResult<AuthResult> {
        return try {
            val authResultTask = auth.signInWithEmailAndPassword(email, password)
            authResultTask.await() // Wait for the task to complete
            // If you reach this point, the task is considered successful
            RequestResult.Success(authResultTask.result)
        } catch (e: Exception) {
            // Handle exceptions and errors here
            RequestResult.Error(ErrorResponse.Default(e.localizedMessage ?: "Login problem"), 0)
        }
    }

/*    override suspend fun loginUser(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { exception ->
                RequestResult.Success(exception)
            }
            .addOnFailureListener { authResult ->
                RequestResult.Error(ErrorResponse.Default(authResult.localizedMessage ?: "Login problem"), 0)
            }
    }*/

    override suspend fun resetPassword(email: String): Task<RequestResult<Unit>> {
        val resetResultTask = auth.sendPasswordResetEmail(email)
        return resetResultTask.continueWith { task ->
            if (task.isSuccessful) {
                RequestResult.Success(Unit)
            } else {
                RequestResult.Error(ErrorResponse.Default("Reset problem"), 0)
            }
        }
    }

    override suspend fun getUserUd(): String? {
        return auth.uid
    }

}