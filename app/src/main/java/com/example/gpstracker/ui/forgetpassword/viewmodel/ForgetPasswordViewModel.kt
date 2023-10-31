package com.example.gpstracker.ui.forgetpassword.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gpstracker.network.RequestResult
import com.example.gpstracker.usecase.FirebaseAuthenticationUseCase
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch
import javax.inject.Inject

class ForgetPasswordViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuthenticationUseCase
): ViewModel() {

    private val resetPasswordResult = MutableLiveData<RequestResult<Unit>>()
    fun getResetPasswordResultLiveData(): LiveData<RequestResult<Unit>> {
        return resetPasswordResult
    }


    fun resetPassword(text: String) {
        viewModelScope.launch {
            val response = firebaseAuth.resetPassword(text)
            checkEmailResponse(response)
        }

    }

    private fun checkEmailResponse(response: Task<RequestResult<Unit>>) {
        response.addOnCompleteListener { task ->
            val result = task.result
            when (result) {
                is RequestResult.Success -> {
                    resetPasswordResult.value = result
                }
                is RequestResult.Error -> {
                    resetPasswordResult.postValue(RequestResult.Error(result.errorData, result.code))
                }
                is RequestResult.Loading -> Unit
            }
        }
    }

}
