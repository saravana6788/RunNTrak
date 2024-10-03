package com.skcodes.core.domain.util

sealed interface DataError:Error {
    enum class NetworkError:DataError{
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        CONFLICT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION_ERROR
    }
    enum class Local:DataError{
        DISK_FULL
    }
}