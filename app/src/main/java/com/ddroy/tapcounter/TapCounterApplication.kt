package com.ddroy.tapcounter

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class TapCounterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}