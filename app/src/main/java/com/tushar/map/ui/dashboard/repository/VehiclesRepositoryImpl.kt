package com.tushar.map.ui.dashboard.repository

import com.tushar.map.ui.base.BaseRepository
import com.tushar.map.ui.dashboard.response.VehiclesInfoResponse
import com.tushar.map.ui.dashboard.service.VehiclesService
import com.tushar.map.utils.AccessTokenDataStore
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


class VehiclesRepositoryImpl  @Inject constructor(
    private val service: VehiclesService,
    private val dataStore: AccessTokenDataStore
) : BaseRepository(), VehiclesRepository {

    override fun tokenData() = dataStore.tokenFlow
    override suspend fun getVehicles(onComplete: (List<VehiclesInfoResponse>) -> Unit) {
        var resource : List<VehiclesInfoResponse> = ArrayList<VehiclesInfoResponse>()
        tokenData().collect {
            resource = service.getVehicles(it.token)
            onComplete(resource)
        }
    }
}