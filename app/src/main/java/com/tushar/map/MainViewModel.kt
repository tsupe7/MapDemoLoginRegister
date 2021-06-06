package com.tushar.map

import androidx.lifecycle.viewModelScope
import com.tushar.map.ui.base.BaseViewModel
import com.tushar.map.ui.dashboard.repository.UserRepository
import com.tushar.map.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {

}