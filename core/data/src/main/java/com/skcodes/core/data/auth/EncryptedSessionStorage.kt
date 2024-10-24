package com.skcodes.core.data.auth

import android.content.SharedPreferences
import com.skcodes.core.domain.AuthInfo
import com.skcodes.core.domain.SessionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EncryptedSessionStorage(
    private val sharedPreferences: SharedPreferences
):SessionStorage {
    override suspend fun get(): AuthInfo? {
        return withContext(Dispatchers.IO){
            val json = sharedPreferences.getString(AUTH_INFO_KEY,null)
            json?.let {
                Json.decodeFromString<AuthInfoSerializable>(it).toAuthInfo()
            }
        }
    }

    override suspend fun set(authInfo: AuthInfo?) {
        withContext(Dispatchers.IO){
            if(authInfo == null){
                sharedPreferences.edit().remove(AUTH_INFO_KEY).commit()
                return@withContext
            }

            val json = Json.encodeToString(authInfo.toAuthInfoSerializable())
            sharedPreferences.edit().putString(AUTH_INFO_KEY,json).commit()
        }

    }


    companion object{
        const val AUTH_INFO_KEY = "AUTH_INFO_KEY"
    }
}