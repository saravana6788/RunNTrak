package com.skcodes.presentation.login

sealed interface LoginAction {
    data object onLoginButtonClick: LoginAction
    data object onPasswordVisibilityButtonClick:LoginAction
    data object onSignUpClick:LoginAction
}