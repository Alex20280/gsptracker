package com.example.gpstracker.ui.forgetpassword.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gpstracker.network.RequestResult
import com.example.gpstracker.usecase.ResetPasswordUseCase
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch
import javax.inject.Inject

class ForgetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
): ViewModel() {

    private val resetPasswordResult = MutableLiveData<RequestResult<Unit>>()
    fun getResetPasswordResultLiveData(): LiveData<RequestResult<Unit>> {
        return resetPasswordResult
    }


    fun resetPassword(text: String) {
        viewModelScope.launch {
            val response = resetPasswordUseCase.resetPassword(text)
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
                    resetPasswordResult.value = RequestResult.Error(result.errorData, result.code)
                }
                is RequestResult.Loading -> Unit
            }
        }
    }

}
