package com.skcodes.auth.data

import com.skcodes.auth.data.model.RegisterRequest
import com.skcodes.auth.domain.AuthRepository
import com.skcodes.core.data.networking.post
import com.skcodes.core.domain.util.DataError
import com.skcodes.core.domain.util.EmptyResult
import io.ktor.client.HttpClient



class AuthRepositoryImpl(
    private val httpClient: HttpClient
):AuthRepository {
    override suspend fun register(email: String, password: String): EmptyResult<DataError.NetworkError> {
        return httpClient.post<RegisterRequest,Unit>(
            route = "/register",
            body = RegisterRequest(
                email = email,
                password = password
            )
        )
    }

}