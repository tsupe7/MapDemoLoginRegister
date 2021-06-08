package com.tushar.map.ui.dashboard.viewmodel

import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val repository: UserRepository) : BaseViewModel() {

    var displayName = MutableLiveData<String>()
    var emailId = MutableLiveData<String>()

    fun retrieveData() {
        viewModelScope.launch {
            repository.displayName().collect{
              displayName.postValue( it.displayName)
            }

            repository.emailId().collect{
                emailId.postValue(it.emailId)
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