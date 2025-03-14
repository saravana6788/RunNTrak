package com.skcodes.presentation.designsystem.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skcodes.presentation.designsystem.R
import com.skcodes.presentation.designsystem.RunNTrakBlack
import com.skcodes.presentation.designsystem.RunNTrakGray
import com.skcodes.presentation.designsystem.RunNTrakTheme

@Composable
fun RunNTrakActionButton(
    modifier: Modifier,
    text:String,
    enabled:Boolean = true,
    isLoading:Boolean,
    onClick:() -> Unit

){
    Button(onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = RunNTrakGray,
            disabledContentColor = RunNTrakBlack

        ),
        shape = RoundedCornerShape(100f),
        modifier = modifier.height(IntrinsicSize.Min)

    ) {
        Box(modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
            ){
            CircularProgressIndicator(
                modifier = Modifier
                    .size(15.dp)
                    .alpha(if (isLoading) 1f else 0f),
                strokeWidth = 1.5.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(text= text,
                modifier = Modifier
                    .alpha(if(isLoading) 0f else 1f),
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview
@Composable
fun RunNTrakActionButtonPreview(){
    RunNTrakTheme {
        RunNTrakActionButton(modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.sign_up),
            isLoading = false
            ) {

        }
    }

}


@Composable
fun RunNTrakOutlinedActionButton(
    modifier: Modifier = Modifier,
    text:String,
    enabled:Boolean = true,
    isLoading:Boolean,
    onClick:() -> Unit

){
    OutlinedButton(onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        border = BorderStroke(0.5.dp,
            color=MaterialTheme.colorScheme.onBackground),

        shape = RoundedCornerShape(100f),
        modifier = modifier.height(IntrinsicSize.Min)

    ) {
        Box(modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(
                modifier = Modifier
                    .size(15.dp)
                    .alpha(if (isLoading) 1f else 0f),
                strokeWidth = 1.5.dp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(text= text,
                modifier = Modifier
                    .alpha(if(isLoading) 0f else 1f),
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
fun RunNTrakOutlinedActionButtonPreview(){
    RunNTrakTheme {
        RunNTrakOutlinedActionButton(modifier = Modifier.fillMaxWidth(),
            text = "Sign In",
            isLoading = false
        ) {

        }
    }

}