package com.skcodes.presentation.login

import com.skcodes.presentation.ui.UIText

sealed interface LoginEvent {
    data object LoginSuccess: LoginEvent
    data class Error(val error: UIText): LoginEvent

}