package com.tushar.login.repository

import com.tushar.map.ui.login.model.request.LoginUserRequest
import com.tushar.map.ui.login.model.response.LoginUserResponse

interface LoginRepository {
    /**
     * Logs in user
     *
     * @param request to login
     */
    suspend fun loginUser(request: LoginUserRequest): LoginUserResponse
}