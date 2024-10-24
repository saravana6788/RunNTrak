package com.skcodes.presentation.register

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
import com.skcodes.presentation.R
import com.skcodes.presentation.ui.UIText
import com.skcodes.presentation.ui.asUIText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
class RegisterViewModel(private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository):ViewModel() {
    var state by mutableStateOf(RegisterState())
        private set

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()
    init{
        state.email.textAsFlow().onEach { email ->
            val isValidEmail = userDataValidator.isValidEmail(email.toString())
            state = state.copy(
                isEmailValid = isValidEmail,
                canRegister = isValidEmail && !state.isRegistering && state.passwordValidationState.isPasswordValid
            )
        }.launchIn(viewModelScope)

        state.password.textAsFlow().onEach { password ->
            val passwordValidationState = userDataValidator.isValidPassword(password.toString())
            state = state.copy(
                passwordValidationState =  passwordValidationState,
                        canRegister = passwordValidationState.isPasswordValid && !state.isRegistering && state.isEmailValid
            )
        }.launchIn(viewModelScope)
    }

    fun  onAction(action: RegisterAction){
        when(action){
            RegisterAction.OnRegisterClickAction -> register()
            RegisterAction.OnTogglePasswordVisibilityClick -> state = state.copy(
                isPasswordVisible =  ! state.isPasswordVisible
            )
            else -> Unit
        }

    }

    fun register(){
        viewModelScope.launch {
            state = state.copy(isRegistering =  true)
            val result = authRepository.register(
                email = state.email.text.toString(),
                password = state.password.text.toString()
            )

            state = state.copy(isRegistering =  false)

            when(result){
                is Result.Error -> {
                    if(result.error == DataError.NetworkError.CONFLICT){
                        eventChannel.send(RegisterEvent.Error(UIText.StringResource(R.string.email_already_exists)))
                    }else{
                        eventChannel.send(RegisterEvent.Error(result.error.asUIText()))
                    }

                }
                is Result.Success ->
                    eventChannel.send(RegisterEvent.RegistrationSuccess)
            }
        }
    }

}