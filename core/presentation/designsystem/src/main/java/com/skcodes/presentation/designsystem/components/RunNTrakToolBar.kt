package com.skcodes.presentation.designsystem.components

import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skcodes.presentation.designsystem.AnalyticsIcon
import com.skcodes.presentation.designsystem.ArrowLeftIcon
import com.skcodes.presentation.designsystem.LogoIcon
import com.skcodes.presentation.designsystem.Poppins
import com.skcodes.presentation.designsystem.R
import com.skcodes.presentation.designsystem.RunNTrakGreen
import com.skcodes.presentation.designsystem.RunNTrakTheme
import com.skcodes.presentation.designsystem.components.util.DropDownItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunNTrakToolBar(
    title:String,
    showBackButton:Boolean,
    onBackClick:()->Unit = {},
    modifier: Modifier = Modifier,
    menuItems:List<DropDownItem> = emptyList(),
    onMenuItemClick:(Int) -> Unit = {},
    scrollBehavior:TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    startContent: @Composable () -> Unit
    ) {

    var isDropDownOpen by rememberSaveable {
        mutableStateOf(false)
    }

    TopAppBar(title = {
        Row(verticalAlignment = Alignment.CenterVertically){
            startContent?.invoke()
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground)
        }
    },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            if(showBackButton){
                IconButton(onClick = onBackClick ) {
                    Icon(
                        imageVector = ArrowLeftIcon , contentDescription = stringResource(id = R.string.go_back),
                        tint = MaterialTheme.colorScheme.onBackground)
                }
            }
        },
        actions = {
            if(menuItems.isNotEmpty()) {
                DropdownMenu(expanded = isDropDownOpen,
                    onDismissRequest = { isDropDownOpen = false }) {
                    menuItems.forEachIndexed{ index, menu ->
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {onMenuItemClick(index)}
                            .fillMaxWidth()
                            .padding(16.dp)){
                        Icon(imageVector = menu.icon, contentDescription = menu.title )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = menu.title,
                            color = MaterialTheme.colorScheme.onBackground)
                    }

                    }
                }
                Box {
                    IconButton(onClick = { isDropDownOpen = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(id = R.string.open_menu),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RunNTrakToolBarPreview(){
    RunNTrakTheme {
        RunNTrakToolBar(
            title = "RunNTrak",
            showBackButton = false,
            modifier = Modifier.fillMaxWidth(),
            startContent = {
                Icon(imageVector = LogoIcon, contentDescription = "",
                    modifier = Modifier.size(35.dp),
                    tint = RunNTrakGreen)
            },
            menuItems = listOf(
                DropDownItem(
                icon = AnalyticsIcon,
                    title = "Analytics"
            )
            )
        )
    }

}