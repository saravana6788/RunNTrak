package com.skcodes.run.presentation.maps

import androidx.compose.ui.graphics.Color
import com.skcodes.core.domain.Location

data class PolyLineUi(
    val location1:Location,
    val location2:Location,
    val color:Color
)
