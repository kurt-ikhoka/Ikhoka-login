package com.ikhoka.login

import com.ikhoka.core.Resource
import com.ikhoka.login.meta.LoginRequest
import com.ikhoka.login.meta.LoginState
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun login(request: LoginRequest): Flow<Resource<LoginState>>
    suspend fun logout(): Flow<Resource<LoginState>>
}