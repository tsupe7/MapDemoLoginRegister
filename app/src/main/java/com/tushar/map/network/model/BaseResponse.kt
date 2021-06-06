package com.tushar.network.model


interface BaseResponse<D, E> {
    val code: Int
    val success: Boolean
    val data: D
    val error: E
}