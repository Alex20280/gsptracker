package com.example.gpstracker.ui.signin.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gpstracker.network.RequestResult
import com.example.gpstracker.prefs.DataStorePreference
import com.example.gpstracker.usecase.GetUserUidUseCase
import com.example.gpstracker.usecase.LoginUserUseCase
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val getUserUidUseCase: GetUserUidUseCase,
    private val dataStorePreference: DataStorePreference
): ViewModel() {

    private val signInResult = MutableLiveData<RequestResult<AuthResult>>()
    fun getSignInResultLiveData(): LiveData<RequestResult<AuthResult>> {
        return signInResult
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            val response = loginUserUseCase.loginUser(email, password)
            Log.d("logCred", email + password)
            checkEmailResponse(response)
        }
    }

    private fun checkEmailResponse(response: RequestResult<AuthResult>) {
            viewModelScope.launch {
                when (response) {
                    is RequestResult.Success -> {
                        // Handle successful login
                        // You can update your UI or perform other actions here
                        signInResult.value = response
                        saveUserId(getUserUidUseCase.getUserUid().toString())
                    }
                    is RequestResult.Error -> {
                        // Handle login error
                        // You can display an error message to the user or perform other error handling here
                        signInResult.postValue(RequestResult.Error(response.errorData, response.code))
                    }
                    is RequestResult.Loading -> Unit
                }
            }
        }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun saveUserId(id: String) {
        viewModelScope.launch {
            dataStorePreference.saveData("UID", id)
        }

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
}