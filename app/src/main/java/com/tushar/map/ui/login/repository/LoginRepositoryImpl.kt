package com.tushar.login.repository

import com.tushar.map.ui.base.BaseRepository
import com.tushar.map.ui.login.model.request.LoginUserRequest
import com.tushar.map.ui.login.model.response.LoginUserResponse
import com.tushar.map.ui.login.service.LoginService
import com.tushar.map.utils.AccessTokenDataStore
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val service: LoginService,
    private val dataStore: AccessTokenDataStore
) : BaseRepository(), LoginRepository {

    override suspend fun loginUser(request: LoginUserRequest): LoginUserResponse {
        val resource = service.loginUser(request)

        if (resource.authenticationToken!=null) {
            dataStore.updateUserLoggedInStatus(true)
            dataStore.updateToken(resource.authenticationToken)
        }
        return resource
    }

}