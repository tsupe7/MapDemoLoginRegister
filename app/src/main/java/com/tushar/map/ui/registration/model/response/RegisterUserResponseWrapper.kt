package com.tushar.map.ui.registration.model.response

import com.tushar.network.model.BaseResponse
import com.tushar.network.model.NetworkError

data class RegisterUserResponseWrapper(
    override val code: Int,
    override val success: Boolean,
    override val data: RegisterUserResponse,
    override val error: NetworkError
) : BaseResponse<RegisterUserResponse, NetworkError>