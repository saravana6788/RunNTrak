package com.skcodes.core.data.networking

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenResponse(
    val accessToken:String,
    val expirationTimestamp:Long
)
