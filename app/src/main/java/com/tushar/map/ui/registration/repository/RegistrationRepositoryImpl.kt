package com.tushar.registration.repository


import com.tushar.map.ui.base.BaseRepository
import com.tushar.map.ui.registration.model.request.RegisterUserRequest
import com.tushar.map.ui.registration.model.response.RegisterUserResponse
import com.tushar.map.ui.registration.service.RegistrationService
import com.tushar.map.utils.AccessTokenDataStore
import javax.inject.Inject

class RegistrationRepositoryImpl @Inject constructor(
    private val service: RegistrationService,
    private val dataStore: AccessTokenDataStore
) : RegistrationRepository, BaseRepository() {

    override suspend fun registerUser(user: RegisterUserRequest): RegisterUserResponse {
        val resource = service.registerUser(user)
        if (resource.authenticationToken!=null) {
            dataStore.updateUserLoggedInStatus(true)
            dataStore.updateToken(resource.authenticationToken)
            dataStore.saveDisplayName(resource.person.displayName)
        }
        return resource
    }
}