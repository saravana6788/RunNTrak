package com.skcodes.auth.domain

import com.skcodes.core.domain.util.DataError
import com.skcodes.core.domain.util.EmptyResult

interface AuthRepository {
    suspend fun register(email:String, password:String):EmptyResult<DataError.NetworkError>
    suspend fun login(email:String,password:String):EmptyResult<DataError.NetworkError>
}