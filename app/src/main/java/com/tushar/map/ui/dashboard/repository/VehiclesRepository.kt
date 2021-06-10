package com.tushar.map.ui.dashboard.repository

import com.tushar.map.ui.dashboard.model.AccessToken
import com.tushar.map.ui.dashboard.response.VehiclesInfoResponse
import kotlinx.coroutines.flow.Flow


interface VehiclesRepository {
    suspend fun getVehicles(onComplete: (List<VehiclesInfoResponse>) ->Unit)
    /**
     * Returns token
     */
    fun tokenData() : Flow<AccessToken>

}