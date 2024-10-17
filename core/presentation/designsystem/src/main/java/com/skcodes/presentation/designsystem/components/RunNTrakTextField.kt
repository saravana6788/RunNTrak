package com.skcodes.presentation.designsystem.components

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
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skcodes.presentation.designsystem.CheckIcon
import com.skcodes.presentation.designsystem.EmailIcon
import com.skcodes.presentation.designsystem.RunNTrakTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RunNTrakTextField(
    textFieldState: TextFieldState = TextFieldState(),
    hint:String? = null,
    title:String? = null,
    startIcon:ImageVector?,
    endIcon:ImageVector?,
    additionalInfo:String? = null,
    modifier: Modifier = Modifier,
    error:String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
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

            if(error!=null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )

            }else if(additionalInfo!=null){
                Text(text = additionalInfo,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp)
            }

        }
        Spacer(
            modifier = Modifier.height(4.dp)
        )
        BasicTextField2(state = textFieldState,
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            lineLimits = TextFieldLineLimits.SingleLine,
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
                .padding(12.dp)
                .onFocusChanged { isFocused = it.isFocused },
            decorator = { innerBox ->
                Row (modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    if(startIcon!=null){
                        Icon(imageVector = startIcon, contentDescription = "",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    Box(modifier = Modifier.weight(1f)){
                        if(textFieldState.text.isEmpty() && !isFocused){
                            Text(text = hint!!,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                    alpha = 0.4f
                                )
                            )
                        }
                        innerBox()
                    }

                    if(endIcon!=null){
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(imageVector = endIcon, contentDescription = "" ,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(end = 8.dp))
                    }



                }

            }

        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun RunNTrakTextFieldPreview(){
    RunNTrakTheme {
        RunNTrakTextField(hint = "example@test.com",
            title ="email" ,
            startIcon = EmailIcon,
            endIcon = CheckIcon, modifier = Modifier.fillMaxWidth(),
            textFieldState =  rememberTextFieldState(),
            additionalInfo = "Enter a valid email",
            )
    }
}