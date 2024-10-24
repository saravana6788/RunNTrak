package com.skcodes.core.data.networking

data class AccessTokenRequest(
    val refreshToken:String,
    val userId:String
)
