package com.skcodes.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skcodes.presentation.designsystem.RunIcon
import com.skcodes.presentation.designsystem.RunNTrakTheme

@Composable
fun RunNTrakFloatingActionButton(
    modifier: Modifier = Modifier,
    icon:ImageVector,
    size:Dp = 25.dp,
    onClick:()->Unit,
    contentDescription:String? = null
){
    Box(modifier = Modifier
        .size(75.dp)
        .clip(CircleShape)
        .clickable { onClick() }
        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        .padding(12.dp),
        contentAlignment = Alignment.Center
        ){

        Box(modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
            ){
            Icon(imageVector = icon, contentDescription = contentDescription,
                tint =MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(size)
                )
        }
    }
}

@Preview
@Composable
fun RunNTrakFloatingActionButtonPreview(){

    RunNTrakTheme {
        RunNTrakFloatingActionButton(
            icon = RunIcon,
            onClick = {},
            size = 25.dp,
            contentDescription = ""
        )
    }

}