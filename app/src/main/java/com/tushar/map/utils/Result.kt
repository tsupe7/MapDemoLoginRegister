package com.tushar.map.utils


data class Result<out T> private constructor(val status: Status, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T? = null): Result<T> =
            Result(Status.SUCCESS, data, null)

        fun <T> error(msg: String, data: T? = null): Result<T> =
            Result(Status.ERROR, data, msg)

        fun <T> noInternet(data: T? = null): Result<T> =
            Result(Status.NO_INTERNET, data, null)

        fun <T> loading(data: T? = null): Result<T> =
            Result(Status.LOADING, data, null)

        fun <T> unknown(data: T? = null): Result<T> =
            Result(Status.UNKNOWN, data, null)
    }
}