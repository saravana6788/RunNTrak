package com.skcodes.run.presentation.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import java.security.Permissions


fun ComponentActivity.shouldShowLocationPermissionRationale():Boolean{
    return shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
}

fun ComponentActivity.shouldShowNotificationPermissionRationale():Boolean{
    return if(Build.VERSION.SDK_INT >= 33)
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
            else
                true
}


private fun Context.hasPermissions(permission:String):Boolean{
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.hasLocationPermissions():Boolean{
    return hasPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
}

fun Context.hasNotificationPermissions():Boolean{
    return if(Build.VERSION.SDK_INT >=33)
        hasPermissions(Manifest.permission.POST_NOTIFICATIONS)
    else true
}