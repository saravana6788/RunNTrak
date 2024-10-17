package com.skcodes.presentation.register

sealed interface RegisterAction {
    data object OnLoginClickAction:RegisterAction
    data object OnTogglePasswordVisibilityClick:RegisterAction
    data object OnRegisterClickAction:RegisterAction

}