package com.skcodes.analytics.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skcodes.presentation.designsystem.RunNTrakTheme

@Composable
fun AnalyticsCard(
    title:String,
    value:String,
    modifier: Modifier = Modifier
){

    Column (modifier = modifier
        .clip(RoundedCornerShape(15.dp))
        .background(color= MaterialTheme.colorScheme.surface)
        .padding(16.dp),
        verticalArrangement = Arrangement.Center){

        Text(text = title,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp)
        
        Spacer(modifier = Modifier.height(4.dp))

        Text(text = value,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 12.sp)
    }

}

@Preview
@Composable
fun AnalyticsCardPreview(){

    RunNTrakTheme {
        AnalyticsCard(
            title = "Total Distance Run",
            value = "23.0"
        )
    }

}