package com.ddroy.tapcounter.utils

import android.app.Activity
import android.view.WindowManager

class ScreenManager(private val activity: Activity?) {
    fun keepScreenOn(enabled: Boolean) {
        if (enabled) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}