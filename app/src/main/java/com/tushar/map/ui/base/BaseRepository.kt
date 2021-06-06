package com.tushar.map.ui.base

import com.tushar.base.resource.BaseError
import com.tushar.base.resource.FailureStatusCode
import com.tushar.base.resource.Resource
import com.tushar.network.exception.NetworkException

abstract class BaseRepository {

    suspend fun <T> perform(asyncAction: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(asyncAction())
        } catch (e: NetworkException) {
            Resource.Failure(
                FailureStatusCode.CLIENT_ERROR,
                BaseError(e.error.code, e.error.error.message)
            )
        }
    }
}