package com.tushar.map.ui.registration.viewmodel

import androidx.lifecycle.viewModelScope
import com.tushar.map.ui.base.BaseViewModel
import com.tushar.map.ui.login.model.request.LoginUserRequest
import com.tushar.map.ui.registration.model.request.RegisterUserRequest
import com.tushar.map.ui.registration.model.response.RegisterUserResponse
import com.tushar.map.utils.Result
import com.tushar.map.utils.SingleLiveEvent
import com.tushar.map.utils.ViewState
import com.tushar.registration.repository.RegistrationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: RegistrationRepository,

    ) : BaseViewModel() {

    private val _registerUserState = SingleLiveEvent<Result<RegisterUserResponse>>()
    val registerUserState: SingleLiveEvent<Result<RegisterUserResponse>> = _registerUserState

    fun registerUser(request: RegisterUserRequest){
        viewModelScope.launch {
            registerUserState.postValue(Result.loading(null))
            try {
                val response = repository.registerUser(request)
                registerUserState.postValue(Result.success(response))
            } catch (e: Exception) {
                registerUserState.postValue(Result.error(e.toString(), null))
            }
        }
    }
}
