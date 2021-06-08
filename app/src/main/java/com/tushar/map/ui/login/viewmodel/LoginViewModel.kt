package com.tushar.map.ui.login.viewmodel


import androidx.lifecycle.viewModelScope
import com.tushar.login.repository.LoginRepository
import com.tushar.map.ui.base.BaseViewModel
import com.tushar.map.ui.dashboard.response.UserInfoResponse
import com.tushar.map.ui.login.model.request.LoginUserRequest
import com.tushar.map.ui.login.model.response.LoginUserResponse
import com.tushar.map.utils.Result
import com.tushar.map.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : BaseViewModel() {

    private val _loginUserState = SingleLiveEvent<Result<LoginUserResponse>>()
    val loginUserState: SingleLiveEvent<Result<LoginUserResponse>> = _loginUserState

    private val _userInfo = SingleLiveEvent<Result<UserInfoResponse>>()
    val userInfo: SingleLiveEvent<Result<UserInfoResponse>> = _userInfo

    fun loginUser(request: LoginUserRequest){
        viewModelScope.launch {
            loginUserState.postValue(Result.loading(null))
            try {
                val loginResponse = loginRepository.loginUser(request)
                loginUserState.postValue(Result.success(loginResponse))
            } catch (e: Exception) {
                loginUserState.postValue(Result.error(e.toString(), null))
            }

            try {

                val userResponse = userRepository.getUser()
                userInfo.postValue(Result.success(userResponse))
            }
            catch (e: Exception){

            }
        }
    }
}