package com.skcodes.run.presentation.active_run

import com.skcodes.presentation.ui.UIText


sealed  interface ActiveRunEvent {

    data class Error(val error:UIText):ActiveRunEvent

    data object RunSaved:ActiveRunEvent


}