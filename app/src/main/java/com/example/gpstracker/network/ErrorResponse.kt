package com.example.gpstracker.network

sealed interface ErrorResponse {

    fun getErrorMessage(): String

    data class Default(
        val message: String
    ) : ErrorResponse {
        override fun getErrorMessage() = message
    }

    data class NotAcceptable(
        val message: String,
        val status: String? = null,
        val error: String? = null
    ) : ErrorResponse {
        override fun getErrorMessage() = message
    }
}