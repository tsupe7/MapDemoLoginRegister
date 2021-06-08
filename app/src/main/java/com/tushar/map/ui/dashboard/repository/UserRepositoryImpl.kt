package com.tushar.map.ui.dashboard.repository

import com.tushar.map.ui.base.BaseRepository
import com.tushar.map.ui.dashboard.model.UserData
import com.tushar.map.ui.dashboard.response.UserInfoResponse
import com.tushar.map.ui.dashboard.service.UserService
import com.tushar.map.utils.AccessTokenDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


class UserRepositoryImpl  @Inject constructor(
    private val service: UserService,
    private val dataStore: AccessTokenDataStore
) : BaseRepository(), UserRepository {

    lateinit var resource : UserInfoResponse
    override fun userLoggedInData() = dataStore.userLoggedInFlow
    override fun logInUserData(): Flow<UserData> = dataStore.loginUserDataFlow
    override fun tokenData() = dataStore.tokenFlow

    override suspend fun getUser(): UserInfoResponse {

        tokenData().collect {
            resource = service.getUser(it.token)

            if(resource.displayName!=null){
                val userData = UserData(resource.displayName, resource.email, resource.createdAt)
                dataStore.loginUserData(userData)
            }
        }
        return resource
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