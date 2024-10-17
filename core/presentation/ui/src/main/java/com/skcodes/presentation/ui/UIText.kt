package com.skcodes.presentation.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

sealed interface UIText {
    data class DynamicString(val text:String):UIText

    data class StringResource(
        @StringRes val id:Int,
        var args:Array<Any> = emptyArray()
    ):UIText

    @Composable
    fun asString():String{
        return when(this){
            is DynamicString -> text
            is StringResource -> LocalContext.current.getString(id,*args)
        }
    }

    fun asString(context: Context): String {
        return when(this) {
            is DynamicString -> text
            is StringResource -> context.getString(id, *args)
        }
    }
}

