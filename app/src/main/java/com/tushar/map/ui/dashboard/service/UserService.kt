package com.tushar.map.ui.dashboard.service

import com.tushar.map.ui.dashboard.response.UserInfoResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface UserService {
    @GET("/api/v2/people/me")
    suspend fun getUser(
        @Header("Authorization")  authorization: String
    ): UserInfoResponse

}