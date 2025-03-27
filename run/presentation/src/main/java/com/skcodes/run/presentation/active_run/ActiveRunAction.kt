package com.skcodes.run.presentation.active_run

sealed interface ActiveRunAction  {
    data object OnToggleRunClick:ActiveRunAction
    data object OnBackClick:ActiveRunAction
    data object OnFinishClick:ActiveRunAction
    data object OnResumeClick:ActiveRunAction

    data class SubmitRequestLocationPermissionInfo(
        val acceptedLocationPermissions:Boolean,
        val shouldShowLocationPermissionRationale:Boolean
    ):ActiveRunAction

    data class SubmitNotificationPermissionInfo(
        val acceptedNotificationPermissions: Boolean,
        val shouldShowNotificationPermissionRationale: Boolean
    ):ActiveRunAction

    data object OnDismissDialog:ActiveRunAction

    class OnRunProcessed(val mapPicInBytes:ByteArray):ActiveRunAction
}