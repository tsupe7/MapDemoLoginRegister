package com.tushar.map.ui.dashboard.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tushar.map.ui.base.BaseViewModel
import com.tushar.map.ui.dashboard.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val repository: UserRepository) : BaseViewModel() {

    var displayName = MutableLiveData<String>()
    var emailId = MutableLiveData<String>()
    var createdDateString = MutableLiveData<String>()


    fun retrieveData() {
        viewModelScope.launch {
            repository.logInUserData().collect{
              displayName.postValue( it.displayName)
              emailId.postValue( it.emailId)
              createdDateString.postValue( it.createDate)
            }
        }
    }

    fun createDate(createdDateString: String): Long{
        var diff: Long =0L
        try {
            val format = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US
            )
            format.timeZone = TimeZone.getTimeZone("UTC")
            val createDate = format.parse(createdDateString)
            val currentDate = Date()
             diff = currentDate.time - createDate.time
        }
        catch (e : Exception){}
        return diff /( 24 *60*60*1000)
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _logOut.value = Unit
        }
    }
}