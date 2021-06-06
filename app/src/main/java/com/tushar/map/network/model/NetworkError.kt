package com.tushar.network.model

data class NetworkError(
    val code: Int,
    val error: Error,
    val success: Boolean
)