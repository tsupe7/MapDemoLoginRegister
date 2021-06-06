package com.tushar.map.ui.login.service

import com.tushar.map.ui.login.model.request.LoginUserRequest
import com.tushar.map.ui.login.model.response.LoginUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("api/v2/people/authenticate")
    suspend fun loginUser(@Body request: LoginUserRequest): LoginUserResponse

}