package com.skcodes.presentation.login

import android.provider.ContactsContract.Data
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.textAsFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skcodes.auth.domain.AuthRepository
import com.skcodes.auth.domain.UserDataValidator
import com.skcodes.core.domain.util.DataError
import com.skcodes.core.domain.util.Result
import com.skcodes.core.domain.util.asEmptyDataResult
import com.skcodes.presentation.R
import com.skcodes.presentation.ui.UIText
import com.skcodes.presentation.ui.asUIText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
class LoginViewModel(
    val authRepository: AuthRepository,
    userDataValidator: UserDataValidator
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val eventsChannel = Channel<LoginEvent>()
    val events = eventsChannel.receiveAsFlow()

init{
    combine(state.email.textAsFlow(),state.password.textAsFlow()){ email,password ->
        state = state.copy(
            canLogin =  userDataValidator.isValidEmail(email.toString()) && password.isNotEmpty()
        )
    }.launchIn(viewModelScope)
}

    fun onAction(action:LoginAction){
        when(action){
            LoginAction.onLoginButtonClick -> login()
            LoginAction.onPasswordVisibilityButtonClick -> state = state.copy(
                isPasswordVisible = !state.isPasswordVisible
            )
            else -> Unit
        }
    }


    fun login(){
        viewModelScope.launch {
            state = state.copy(isLoggingIn = true)
           val response = authRepository.login(state.email.text.toString().trim(),state.password.text.toString())
            state = state.copy(isLoggingIn = false)
            when(response){
                is Result.Error -> {
                    if(response.error  == DataError.NetworkError.UNAUTHORIZED){
                        eventsChannel.send(LoginEvent.Error(
                            UIText.StringResource(R.string.username_password_not_match)
                        ))
                    }else{
                        eventsChannel.send(LoginEvent.Error(response.error.asUIText()))
                    }
                }
                is Result.Success -> eventsChannel.send(LoginEvent.LoginSuccess)
            }

        }
    }
}