package com.example.gpstracker.network

sealed interface ErrorDto {

    fun getErrorMessage(): String

    data class Default(
        val message: String
    ) : ErrorDto {
        override fun getErrorMessage() = message
    }

    data class NotAcceptable(
        val message: String,
        val status: String? = null,
        val error: String? = null
    ) : ErrorDto {
        override fun getErrorMessage() = message
    }
}