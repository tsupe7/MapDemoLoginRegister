package com.tushar.map.ui.login.viewmodel


import androidx.lifecycle.viewModelScope
import com.tushar.login.repository.LoginRepository
import com.tushar.map.ui.base.BaseViewModel
import com.tushar.map.ui.login.model.request.LoginUserRequest
import com.tushar.map.ui.login.model.response.LoginUserResponse
import com.tushar.map.utils.Result
import com.tushar.map.utils.SingleLiveEvent
import com.tushar.map.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) :
    BaseViewModel() {

    private val _loginUserState = SingleLiveEvent<Result<LoginUserResponse>>()
    val loginUserState: SingleLiveEvent<Result<LoginUserResponse>> = _loginUserState

    fun loginUser(request: LoginUserRequest){
        viewModelScope.launch {
            loginUserState.postValue(Result.loading(null))
            try {
                val response = repository.loginUser(request)
                loginUserState.postValue(Result.success(response))
            } catch (e: Exception) {
                loginUserState.postValue(Result.error(e.toString(), null))
            }
        }
    }
}