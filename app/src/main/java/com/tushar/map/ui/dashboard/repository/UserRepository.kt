package com.tushar.map.ui.dashboard.repository

import com.tushar.map.ui.dashboard.model.AccessToken
import com.tushar.map.ui.dashboard.model.IsUserLoggedIn
import com.tushar.map.ui.dashboard.model.UserData
import com.tushar.map.ui.dashboard.response.UserInfoResponse
import kotlinx.coroutines.flow.Flow


interface UserRepository {

    suspend fun getUser(onComplete: (UserInfoResponse) ->Unit)
    /**
     * Returns token
     */
    fun tokenData() : Flow<AccessToken>

    /**
     * Logs out user
     */
    suspend fun logout()

    /**
     * Returns user logged-in data
     */
    fun userLoggedInData() : Flow<IsUserLoggedIn>

    /**
     * Returns user data
     */
    fun logInUserData() : Flow<UserData>
}