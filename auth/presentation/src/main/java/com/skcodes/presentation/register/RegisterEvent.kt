package com.skcodes.presentation.register

import com.skcodes.presentation.ui.UIText

sealed interface RegisterEvent {
    data object RegistrationSuccess: RegisterEvent
    data class Error(val error:UIText): RegisterEvent

}
