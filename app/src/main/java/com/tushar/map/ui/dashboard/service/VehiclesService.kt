package com.tushar.map.ui.dashboard.service

import com.tushar.map.ui.dashboard.response.UserInfoResponse
import com.tushar.map.ui.dashboard.response.VehiclesInfoResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface VehiclesService {
    @GET("/api/v2/vehicles")
    suspend fun getVehicles(
        @Header("Authorization")  authorization: String
    ): List<VehiclesInfoResponse>

}