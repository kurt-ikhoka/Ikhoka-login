package com.ikhoka.login

import com.ikhoka.core.Resource
import com.ikhoka.login.meta.LoginRequest
import com.ikhoka.login.meta.LoginState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class FakeLoginRepository : LoginRepository {
    override suspend fun login(request: LoginRequest) = flow {
        emit(Resource.loading<LoginState>())
        delay(2000)
//        emit(Resource.error(Exception("failed login")))
        emit(Resource.success(LoginState.AUTHENTICATED))
    }


    override suspend fun logout() = flow {
        emit(Resource.loading<LoginState>())
        delay(1000)
        emit(Resource.success(LoginState.UNAUTHENTICATED))
    }
}