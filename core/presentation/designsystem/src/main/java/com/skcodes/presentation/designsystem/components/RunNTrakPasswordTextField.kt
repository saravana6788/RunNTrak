package com.skcodes.presentation.designsystem.components

import android.opengl.Visibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text2.BasicSecureTextField
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.TextObfuscationMode
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skcodes.presentation.designsystem.CheckIcon
import com.skcodes.presentation.designsystem.EmailIcon
import com.skcodes.presentation.designsystem.EyeClosedIcon
import com.skcodes.presentation.designsystem.EyeOpenedIcon
import com.skcodes.presentation.designsystem.LockIcon
import com.skcodes.presentation.designsystem.R
import com.skcodes.presentation.designsystem.RunNTrakTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RunNTrakPasswordTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState = TextFieldState(),
    title:String? = null,
    hint:String?,
    isPassWordVisible:Boolean,
    onTogglePasswordVisibility: ()->Unit
) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier) {
        Row (modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            title?.let {
                Text(text = title,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

        }
        Spacer(
            modifier = Modifier.height(4.dp)
        )
        BasicSecureTextField(state = textFieldState,
            textObfuscationMode = if(isPassWordVisible){
                TextObfuscationMode.Visible
            }else TextObfuscationMode.Hidden,
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (isFocused) MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.05f
                    ) else
                        MaterialTheme.colorScheme.surface
                )

                .border(
                    width = 1.dp,
                    color = if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                )

                .onFocusChanged { isFocused = it.isFocused },
            decorator = { innerBox ->
                Row (modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        imageVector = LockIcon, contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 16.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))


                    Box(modifier = Modifier.weight(1f)) {
                        if (textFieldState.text.isEmpty() && !isFocused) {
                            Text(
                                text = hint!!,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                    alpha = 0.4f
                                )
                            )
                        }
                        innerBox()
                    }

                    IconButton(onClick = onTogglePasswordVisibility) {
                        Icon(
                            imageVector = if (isPassWordVisible) EyeOpenedIcon else EyeClosedIcon,
                            contentDescription = if (isPassWordVisible) stringResource(id = R.string.show_password) else stringResource(
                                id = R.string.hide_password
                            ),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                    }


                }


            }

        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun RunNTrakPasswordTextFieldPreview(){
    RunNTrakTheme {
        RunNTrakPasswordTextField(
            title ="Password" ,
            hint="Password",
            modifier = Modifier.fillMaxWidth(),
            onTogglePasswordVisibility = {},
            isPassWordVisible = false,
            textFieldState =  rememberTextFieldState(),
            )
    }
}