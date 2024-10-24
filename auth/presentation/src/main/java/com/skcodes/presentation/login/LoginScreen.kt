package com.skcodes.presentation.login

import android.widget.Space
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skcodes.presentation.R
import com.skcodes.presentation.designsystem.EmailIcon
import com.skcodes.presentation.designsystem.LockIcon
import com.skcodes.presentation.designsystem.Poppins
import com.skcodes.presentation.designsystem.RunNTrakGray
import com.skcodes.presentation.designsystem.RunNTrakGreen
import com.skcodes.presentation.designsystem.RunNTrakTheme
import com.skcodes.presentation.designsystem.components.GradientBackground
import com.skcodes.presentation.designsystem.components.RunNTrakActionButton
import com.skcodes.presentation.designsystem.components.RunNTrakPasswordTextField
import com.skcodes.presentation.designsystem.components.RunNTrakTextField
import com.skcodes.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration

@Composable
fun LoginScreenRoot(
    onLoginSuccess:() ->Unit,
    onSignUpClick:() -> Unit,
    viewModel: LoginViewModel = koinViewModel()
){

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    ObserveAsEvents(flow = viewModel.events) {event ->
        when(event){
            is LoginEvent.Error ->{
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    LENGTH_LONG).show()
            }

            is LoginEvent.LoginSuccess ->{
                keyboardController?.hide()
                Toast.makeText(context,
                    R.string.you_are_logged_in,
                    LENGTH_LONG).show()
                onLoginSuccess()
            }
        }

    }

    LoginScreen(
        state = viewModel.state,
        onAction = { action ->
            when(action){
                LoginAction.onSignUpClick -> onSignUpClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginScreen(state:LoginState,
                onAction: (LoginAction)->Unit){
    GradientBackground {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(vertical = 32.dp)
            .padding(top = 16.dp)) {

            Text(
                text = stringResource(id = R.string.login_welcome_text),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.login_welcome_description),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(48.dp))

            RunNTrakTextField(
                textFieldState = state.email,
                startIcon = EmailIcon,
                endIcon = null,
                title = stringResource(id = R.string.email),
                hint = stringResource(id = R.string.email_hint),
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            RunNTrakPasswordTextField(
                textFieldState = state.password, hint = stringResource(id = R.string.password),
                isPassWordVisible = state.isPasswordVisible,
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.password),
                onTogglePasswordVisibility = { onAction(LoginAction.onPasswordVisibilityButtonClick) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            RunNTrakActionButton(modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.login),
                isLoading = state.isLoggingIn,
                enabled = state.canLogin && !state.isLoggingIn,
                onClick = { onAction(LoginAction.onLoginButtonClick) })
        }
        val annotatedString = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontFamily = Poppins,
                    color = RunNTrakGray
                )
            ){
                append(text = stringResource(id = R.string.dont_have_an_account)+" ")
                pushStringAnnotation(
                    tag = "clickable_text",
                    annotation = stringResource(id = R.string.sign_up)
                )

                withStyle(
                    style = SpanStyle(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = RunNTrakGreen
                    )
                ){
                    append(text = stringResource(id = R.string.sign_up))
                }
            }
        }


        Box(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .weight(1f)
            .padding(bottom = 54.dp),
            contentAlignment = Alignment.BottomCenter
        ){
            ClickableText(text = annotatedString,
                onClick = {offset ->
                    annotatedString.getStringAnnotations(
                        start = offset,
                        end = offset,
                        tag = "clickable_text"
                    )
                        .firstOrNull()?.let {
                            onAction(LoginAction.onSignUpClick)
                        }

                }
            )
        }
    }
}





@Preview
@Composable
fun LoginScreenPreview(){
    RunNTrakTheme {
        LoginScreen(state = LoginState(),
            onAction = {})
    }

}