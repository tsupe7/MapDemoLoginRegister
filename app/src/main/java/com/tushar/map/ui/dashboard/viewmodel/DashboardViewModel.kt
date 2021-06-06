package com.tushar.map.ui.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.tushar.login.repository.LoginRepository
import com.tushar.map.ui.base.BaseViewModel
import com.tushar.map.ui.dashboard.repository.UserRepository
import com.tushar.map.ui.dashboard.response.UserInfoResponse
import com.tushar.map.ui.login.model.request.LoginUserRequest
import com.tushar.map.ui.login.model.response.LoginUserResponse
import com.tushar.map.utils.Result
import com.tushar.map.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val repository: UserRepository) : BaseViewModel() {

    private val _userInfo = SingleLiveEvent<Result<UserInfoResponse>>()
    val userInfo: SingleLiveEvent<Result<UserInfoResponse>> = _userInfo
    init {
        getUser()
    }

    fun getUser(){
        viewModelScope.launch {
            userInfo.postValue(Result.loading(null))
            try {
                val response = repository.getUser()
                userInfo.postValue(Result.success(response))
            } catch (e: Exception) {
                userInfo.postValue(Result.error(e.toString(), null))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _logOut.value = Unit
        }
    }
}