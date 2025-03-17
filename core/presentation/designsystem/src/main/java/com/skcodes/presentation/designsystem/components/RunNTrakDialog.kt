package com.skcodes.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.skcodes.presentation.designsystem.RunNTrakTheme

@Composable
fun RunNTrakDialog(
    title:String,
    description:String,
    primaryButton: @Composable RowScope.()->Unit,
    secondaryButton: @Composable RowScope.()->Unit = {},
    modifier:Modifier = Modifier,
    onDismiss:() ->Unit = {}
){
    Dialog(onDismissRequest = onDismiss) {
        Column(modifier = modifier
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title,
                fontWeight  = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Text(text = description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )

            Row (modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)){
                secondaryButton()
                primaryButton()

            }

        }
        
    }
    
}

@Preview
@Composable
fun RunNTrakDialogPreview(){
    RunNTrakTheme {
        RunNTrakDialog(title = "Permission Requested",
            description = "Need Location and Notification Permissions",
            primaryButton = { }
        )
    }
    
} 