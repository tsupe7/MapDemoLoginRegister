package com.tushar.network.exception

import com.tushar.network.model.NetworkError


data class NetworkException(val error: NetworkError) : Exception()