package com.skcodes.presentation.ui

import com.skcodes.core.domain.util.DataError

fun DataError.asUIText():UIText{
    return when(this){
        DataError.Local.DISK_FULL -> UIText.StringResource(R.string.error_disk_full)
        DataError.NetworkError.REQUEST_TIMEOUT -> UIText.StringResource(R.string.error_request_timeout)
        DataError.NetworkError.TOO_MANY_REQUESTS -> UIText.StringResource(R.string.error_too_may_requests)
        DataError.NetworkError.NO_INTERNET -> UIText.StringResource(R.string.error_no_internet)
        DataError.NetworkError.PAYLOAD_TOO_LARGE -> UIText.StringResource(R.string.error_payload_too_large)
        DataError.NetworkError.SERVER_ERROR -> UIText.StringResource(R.string.error_server_error)
        DataError.NetworkError.SERIALIZATION_ERROR -> UIText.StringResource(R.string.error_serialization)
        else -> UIText.StringResource(R.string.unknown_error)
    }
}