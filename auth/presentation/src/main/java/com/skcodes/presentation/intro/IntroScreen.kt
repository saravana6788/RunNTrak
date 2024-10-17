package com.skcodes.presentation.intro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skcodes.presentation.R
import com.skcodes.presentation.designsystem.LogoIcon
import com.skcodes.presentation.designsystem.RunNTrakTheme
import com.skcodes.presentation.designsystem.components.GradientBackground
import com.skcodes.presentation.designsystem.components.RunNTrakActionButton
import com.skcodes.presentation.designsystem.components.RunNTrakOutlinedActionButton

@Composable
fun IntroScreenRoot(
    onSignUpClick:()->Unit,
    onSignInClick:()->Unit
){
    IntroScreen (
onAction = { action ->
    when(action){
        IntroAction.OnSignInClick -> onSignInClick()
        IntroAction.OnSignUpClick -> onSignUpClick()
    }
}

    )

}
@Composable
fun IntroScreen(
    onAction: (IntroAction) -> Unit
) {
    GradientBackground {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {

            RunNTrakLogoVertical()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 48.dp)
        ) {
            Text(
                text = stringResource(id = R.string.runntrak_welcome_text),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.runntrak_welcome_description),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(32.dp))

            RunNTrakOutlinedActionButton(modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.sign_in),
                isLoading = false,
                onClick = {
                    onAction(IntroAction.OnSignInClick)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            RunNTrakActionButton(modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.sign_up),
                isLoading = false,
                onClick = {
                    onAction(IntroAction.OnSignUpClick)
                }
            )

        }
    }
}

@Composable
fun RunNTrakLogoVertical(
    modifier:Modifier = Modifier
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Icon(
            imageVector = LogoIcon, contentDescription = "icon",
            tint = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = R.string.runntrak_logo_text),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}





@Preview
@Composable
fun IntroScreenPreview(){
    RunNTrakTheme {
        IntroScreen(
            onAction = {}
        )
    }

}