package com.tushar.map.ui.dashboard.repository

import com.tushar.map.ui.base.BaseRepository
import com.tushar.map.ui.dashboard.model.UserData
import com.tushar.map.ui.dashboard.response.UserInfoResponse
import com.tushar.map.ui.dashboard.response.VehiclesInfoResponse
import com.tushar.map.ui.dashboard.service.UserService
import com.tushar.map.utils.AccessTokenDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


class UserRepositoryImpl  @Inject constructor(
    private val service: UserService,
    private val dataStore: AccessTokenDataStore
) : BaseRepository(), UserRepository {


    override fun userLoggedInData() = dataStore.userLoggedInFlow
    override fun logInUserData(): Flow<UserData> = dataStore.loginUserDataFlow
    override fun tokenData() = dataStore.tokenFlow

    override suspend fun getUser(onComplete: (UserInfoResponse) -> Unit) {
        var resource: UserInfoResponse? = null
        tokenData().collect {
            resource = service.getUser(it.token)
            resource?.let {user->
                val userData = UserData(user.displayName, user.email, user.createdAt)
                dataStore.loginUserData(userData)
            }
            onComplete(resource!!)
        }
    }

    /**
     * Logs out user
     */
    override suspend fun logout() {
        with(dataStore) {
            updateToken("")
            loginUserData(null)
            updateUserLoggedInStatus(loggedIn = false)
        }
    }
}