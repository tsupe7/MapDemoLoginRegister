package com.tushar.map.ui.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tushar.base.resource.BaseError
import com.tushar.base.resource.FailureStatusCode
import com.tushar.base.resource.Resource
import com.tushar.map.ui.dashboard.repository.UserRepository
import com.tushar.map.utils.NetworkUtils
import com.tushar.map.utils.SingleLiveEvent
import com.tushar.map.utils.ViewState
import com.tushar.network.model.BaseResponse
import kotlinx.coroutines.launch
import com.tushar.map.utils.Result
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


abstract class BaseViewModel : ViewModel() {

    @Inject
    protected lateinit var networkUtils: NetworkUtils

    @Inject
    lateinit var userRepository: UserRepository

    protected val _messageStringId: MutableLiveData<Result<Int>> = MutableLiveData()
    val messageStringId: LiveData<Result<Int>>
        get() = _messageStringId

    protected val _messageString: MutableLiveData<Result<String>> = MutableLiveData()
    val messageString: LiveData<Result<String>>
        get() = _messageString

    protected val _logOut: SingleLiveEvent<Unit> = SingleLiveEvent()
    val logOut: LiveData<Unit>
        get() = _logOut

    private val _isLoggedInState = SingleLiveEvent<Boolean>()
    val isLoggedInState: SingleLiveEvent<Boolean> = _isLoggedInState

    /**
     * Method used to perform async task which takes a lot of time
     * such as rest service.
     * This can emit 3 states, LOADING, SUCCESS and FAILURE.
     */
    protected fun <T> getResult(asyncAction: suspend () -> T, onResult: (Resource<T>) -> Unit) {
        viewModelScope.launch {
            try {
                onResult(Resource.Loading())
                val data = asyncAction()
                onResult(Resource.Success(data))
            } catch (exception: Exception) {
                Log.e("", exception.toString())
                val failure = parseException<T>(exception)
                onResult(failure)
            }
        }
    }

    /**
     * Method generally used to perform async tasks related to database.
     * mostly DAO operations.
     */
    protected fun <T> performTask(asyncAction: suspend () -> T, onResult: (T) -> Unit) {
        viewModelScope.launch {
            val result = asyncAction()
            onResult(result)
        }
    }

    protected fun <D, N, T : BaseResponse<D, N>> performRequest(
        asyncAction: suspend () -> Resource<T>,
        state: SingleLiveEvent<ViewState<D>>
    ) {
        viewModelScope.launch {
            if (networkUtils.isConnected()) {
                //Starting loading screen
                state.value = ViewState.loading()
                when (val resource = asyncAction()) {
                    is Resource.Loading -> state.value = ViewState.loading()
                    is Resource.Success -> state.value =
                        ViewState.success(data = resource.data.data)
                    is Resource.Failure -> state.value =
                        ViewState.failed(resource.error.message)
                }
            } else {
                state.value = ViewState.noInternet()
            }
        }
    }

    private fun <T> parseException(exception: Exception): Resource.Failure<T> {
        // Write custom parse exception logic depending on API structure.
        return Resource.Failure(FailureStatusCode.CLIENT_ERROR, BaseError(502, "Error"))
    }

    fun isUserLoggedIn() {
        viewModelScope.launch {
            userRepository.userLoggedInData().collect {
                _isLoggedInState.value = it.loggedIn
            }
        }
    }
}