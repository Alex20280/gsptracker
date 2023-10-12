package com.example.gpstracker.network

sealed class RequestResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : RequestResult<T>()
    data class Error(val errorData: ErrorDto, val code: Int) : RequestResult<Nothing>()
    object Loading: RequestResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$errorData]"
            Loading -> "Loading"
        }
    }
}