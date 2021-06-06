package com.tushar.map.ui.login.model.response

import com.tushar.network.model.BaseResponse
import com.tushar.network.model.NetworkError

data class LoginUserResponseWrapper(
    override val code: Int,
    override val success: Boolean,
    override val data: LoginUserResponse,
    override val error: NetworkError
) : BaseResponse<LoginUserResponse, NetworkError>