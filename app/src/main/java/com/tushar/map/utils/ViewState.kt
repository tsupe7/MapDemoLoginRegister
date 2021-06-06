package com.tushar.map.utils

/**
 * State for managing UI operations.
 */
sealed class ViewState<T> {
    class Loading<T> : ViewState<T>()
    class Success<T>(val data: T) : ViewState<T>()
    class Failed<T>(val message: String) : ViewState<T>()
    class NoInternet<T>() : ViewState<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failed(message: String) = Failed<T>(message)
        fun <T> noInternet() = NoInternet<T>()
    }
}