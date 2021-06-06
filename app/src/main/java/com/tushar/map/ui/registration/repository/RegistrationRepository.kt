package com.tushar.registration.repository

import com.tushar.base.resource.Resource
import com.tushar.map.ui.registration.model.request.RegisterUserRequest
import com.tushar.map.ui.registration.model.response.RegisterUserResponse

interface RegistrationRepository {

    /**
     * Registers user
     *
     * @param user to register
     */
    suspend fun registerUser(user: RegisterUserRequest): RegisterUserResponse

}