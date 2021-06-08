package com.tushar.map.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import com.tushar.map.ui.dashboard.model.AccessToken
import com.tushar.map.ui.dashboard.model.IsUserLoggedIn
import com.tushar.map.ui.dashboard.model.UserData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccessTokenDataStore @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        val USER_LOGGED_IN = booleanPreferencesKey("user_looged_in")
        val USER_TOKEN = stringPreferencesKey("access_token")
        val DISPLAY_NAME = stringPreferencesKey("display_name")
        val EMAIL = stringPreferencesKey("email")
        val CREATED_DATE = stringPreferencesKey("created_date")

    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(name = "user")

    val tokenFlow: Flow<AccessToken> = dataStore.data
        .map { preferences ->
            val token = preferences[USER_TOKEN] ?: ""
            AccessToken(token)
        }

    val userLoggedInFlow: Flow<IsUserLoggedIn> = dataStore.data
        .map { preferences ->
            val loggedIn = preferences[USER_LOGGED_IN] ?: false
            IsUserLoggedIn(loggedIn)
    }

    suspend fun updateUserLoggedInStatus(loggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[USER_LOGGED_IN] = loggedIn
        }
    }

    suspend fun updateToken(token: String?) {
        dataStore.edit { preferences ->
            token?.let {
                preferences[USER_TOKEN] = it
            }
        }
    }

    val loginUserDataFlow: Flow<UserData> = dataStore.data
        .map { preferences ->
            val displayName = preferences[DISPLAY_NAME] ?: ""
            val emailId = preferences[EMAIL] ?: ""
            val createDate = preferences[CREATED_DATE] ?: ""
            UserData(displayName, emailId, createDate)
        }

    suspend fun loginUserData(userData: UserData?) {
        userData?.let {
            dataStore.edit { preferences ->
                preferences[DISPLAY_NAME] = userData.displayName!!
                preferences[EMAIL] = userData.emailId!!
                preferences[CREATED_DATE] = userData.createDate!!
            }
        }
    }
}