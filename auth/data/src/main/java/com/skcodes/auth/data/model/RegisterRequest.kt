package com.skcodes.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email:String,
    val password:String
)
