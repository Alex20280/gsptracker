package com.example.gpstracker.ui.signup.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gpstracker.network.RequestResult
import com.example.gpstracker.usecase.FirebaseAuthenticationUseCase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    private val registrationUseCase: FirebaseAuthenticationUseCase
) : ViewModel() {

    private val signUpResult = MutableLiveData<RequestResult<Task<AuthResult>>>()
    fun getSignUpResultLiveData(): LiveData<RequestResult<Task<AuthResult>>> {
        return signUpResult
    }

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            val response = registrationUseCase.registerUser(email, password)
            checkEmailResponse(response)
        }
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        // Password should be at least 8 characters long
        if (password.length < 8) {
            return false
        }

        // Check for at least one uppercase letter
        val uppercasePattern = "[A-Z]".toRegex()
        if (!uppercasePattern.containsMatchIn(password)) {
            return false
        }

        // Check for at least one digit
        val digitPattern = "\\d".toRegex()
        if (!digitPattern.containsMatchIn(password)) {
            return false
        }

        // Check for at least one special character
        val specialCharacterPattern = "[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]".toRegex()
        if (!specialCharacterPattern.containsMatchIn(password)) {
            return false
        }
        return true
    }

    private fun checkEmailResponse(response: RequestResult<Task<AuthResult>>) {
        when (response) {
            is RequestResult.Success -> {
                signUpResult.value = response
            }
            is RequestResult.Error -> {
                signUpResult.postValue(RequestResult.Error(response.errorData, response.code))
            }
            is RequestResult.Loading -> Unit
        }
    }
}