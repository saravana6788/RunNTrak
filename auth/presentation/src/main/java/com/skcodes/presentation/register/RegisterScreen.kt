package com.skcodes.presentation.register
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import com.skcodes.auth.domain.UserDataValidator
import com.skcodes.presentation.R
import com.skcodes.presentation.designsystem.CheckIcon
import com.skcodes.presentation.designsystem.CrossIcon
import com.skcodes.presentation.designsystem.EmailIcon
import com.skcodes.presentation.designsystem.Poppins
import com.skcodes.presentation.designsystem.RunNTrakDarkRed
import com.skcodes.presentation.designsystem.RunNTrakGray
import com.skcodes.presentation.designsystem.RunNTrakGreen
import com.skcodes.presentation.designsystem.RunNTrakTheme
import com.skcodes.presentation.designsystem.components.GradientBackground
import com.skcodes.presentation.designsystem.components.RunNTrakActionButton
import com.skcodes.presentation.designsystem.components.RunNTrakPasswordTextField
import com.skcodes.presentation.designsystem.components.RunNTrakTextField
import com.skcodes.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel


@Composable
fun RegisterScreenRoot(
    onSignInClick:()->Unit,
    onRegistrationSuccessful:()->Unit,
    viewModel: RegisterViewModel=koinViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    ObserveAsEvents(viewModel.events) { event ->
        when(event){
            is RegisterEvent.Error ->{
                keyboardController?.hide()
                Toast.makeText(context,event.error.asString(context),LENGTH_LONG).show()
            }
            is RegisterEvent.RegistrationSuccess -> {
                Toast.makeText(context, R.string.registration_successful,LENGTH_LONG).show()
                onRegistrationSuccessful()
            }

        }
        
    }
RegisterScreen(
    state = viewModel.state,
    onAction = viewModel::onAction
)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegisterScreen(
    state:RegisterState,
    onAction: (RegisterAction)->Unit
){
    GradientBackground {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(vertical = 32.dp)
            .padding(top = 16.dp)
            ) {
            Text(text = stringResource(id = R.string.create_account),
                style = MaterialTheme.typography.headlineMedium
            )

            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = Poppins,
                        color = RunNTrakGray
                    )
                ){
                    append(text = stringResource(id = R.string.already_have_account)+" ")
                    pushStringAnnotation(
                        tag = "clickable_text",
                        annotation  = stringResource(id = R.string.login)
                    )
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = Poppins
                        )
                    ){
                        append(text = stringResource(id = R.string.login))
                    }
                }
            }

            ClickableText(text = annotatedString,
                onClick = { offset ->
                    annotatedString.getStringAnnotations(
                        tag = "clickable_text",
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        onAction(RegisterAction.OnLoginClickAction)
                    }
                }
            )

            Spacer(modifier = Modifier.height(48.dp))

            RunNTrakTextField(startIcon = EmailIcon,
                endIcon = if(state.isEmailValid)CheckIcon else null,
                hint =  stringResource(id =R.string.email_hint),
                title = stringResource(id = R.string.email),
                additionalInfo = stringResource(id = R.string.enter_a_vaid_email),
                keyboardType = KeyboardType.Email,
                textFieldState = state.email,
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))

            RunNTrakPasswordTextField(hint = stringResource(id = R.string.password),
                title = stringResource(id = R.string.password),
                isPassWordVisible = state.isPasswordVisible,
                onTogglePasswordVisibility = {
                    onAction(RegisterAction.OnTogglePasswordVisibilityClick)
                },
                textFieldState = state.password,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordRequirement(text = stringResource(id = R.string.atleast_min_characters, UserDataValidator.MINIMUM_PASSWORD_LENGTH),
                isValid = state.passwordValidationState.hasMinLength)

            Spacer(modifier = Modifier.height(4.dp))

            PasswordRequirement(text = stringResource(id = R.string.contains_numbers),
                isValid = state.passwordValidationState.hasNumber)

            Spacer(modifier = Modifier.height(4.dp))

            PasswordRequirement(text = stringResource(id = R.string.contains_lower_case_alphabets),
                isValid = state.passwordValidationState.hasLowerCaseCharacter)

            Spacer(modifier = Modifier.height(4.dp))

            PasswordRequirement(text = stringResource(id = R.string.contains_upper_case_alphabets),
                isValid = state.passwordValidationState.hasUpperCaseCharacter)

            Spacer(modifier = Modifier.height(52.dp))

            RunNTrakActionButton(modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.register) ,
                isLoading = state.isRegistering,
                enabled = state.canRegister,
                onClick = {
                    onAction(RegisterAction.OnRegisterClickAction)
                })

        }
    }

}

@Composable
fun PasswordRequirement(
    modifier: Modifier = Modifier,
    text:String,
    isValid:Boolean
){
    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically){
        Icon(imageVector = if(isValid) CheckIcon else CrossIcon, contentDescription = "",
            tint = if(isValid) RunNTrakGreen else RunNTrakDarkRed)

        Spacer(modifier = Modifier.width(16.dp))

        Text(text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp)
    }
}

@Preview
@Composable
fun RegisterScreenPreview(){
    RunNTrakTheme {
        RegisterScreen(state = RegisterState(),
            onAction = {}
        )
    }
}