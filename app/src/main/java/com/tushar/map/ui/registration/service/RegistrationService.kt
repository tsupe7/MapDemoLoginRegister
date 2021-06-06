package com.tushar.map.ui.registration.service

import com.tushar.map.ui.registration.model.request.RegisterUserRequest
import com.tushar.map.ui.registration.model.response.RegisterUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegistrationService {

    @POST("api/v2/people/create")
    suspend fun registerUser(@Body user: RegisterUserRequest): RegisterUserResponse

}