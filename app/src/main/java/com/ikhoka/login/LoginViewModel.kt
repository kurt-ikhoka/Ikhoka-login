package com.ikhoka.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikhoka.core.Resource
import com.ikhoka.login.di.CacheReplay
import com.ikhoka.login.meta.LoginRequest
import com.ikhoka.login.meta.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepo: LoginRepository, @CacheReplay private val stateReplay:Int = 0) : ViewModel() {

    private val _authState = MutableSharedFlow<Resource<LoginState>>(replay = stateReplay)
    val authState: SharedFlow<Resource<LoginState>> = _authState

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            loginRepo.login(request).collect {
                _authState.emit(it)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            loginRepo.logout().collect {
                _authState.emit(it)
            }
        }
    }

}