package com.skcodes.auth.data

import com.skcodes.auth.data.model.LoginRequest
import com.skcodes.auth.data.model.LoginResponse
import com.skcodes.auth.domain.AuthRepository
import com.skcodes.core.data.networking.post
import com.skcodes.core.domain.AuthInfo
import com.skcodes.core.domain.SessionStorage
import com.skcodes.core.domain.util.DataError
import com.skcodes.core.domain.util.EmptyResult
import com.skcodes.core.domain.util.Result
import com.skcodes.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient
import com.skcodes.auth.data.model.RegisterRequest as RegisterRequest1


class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
):AuthRepository {
    override suspend fun register(email: String, password: String): EmptyResult<DataError.NetworkError> {
        return httpClient.post<RegisterRequest1,Unit>(
            route = "/register",
            body = RegisterRequest1(
                email = email,
                password = password
            )
        )
    }

    override suspend fun login(
        email: String,
        password: String
    ): EmptyResult<DataError.NetworkError> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            route = "/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        )

        if(result is Result.Success){
            sessionStorage.set(
                AuthInfo(
                    accessToken =  result.data.accessToken,
                    refreshToken = result.data.refreshToken,
                    userId =  result.data.userId
                )
            )
        }

        return result.asEmptyDataResult()
    }

}