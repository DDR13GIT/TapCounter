package com.ddroy.tapcounter

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.ddroy.tapcounter.constants.ThemeConstants.THEME_BLUE
import com.ddroy.tapcounter.constants.ThemeConstants.THEME_GREEN
import com.ddroy.tapcounter.constants.ThemeConstants.THEME_PINK
import com.ddroy.tapcounter.sharedPreference.PreferenceKeys
import com.ddroy.tapcounter.sharedPreference.ThemeSharedPref
import com.ddroy.tapcounter.utils.ScreenManager
import com.ddroy.tapcounter.viewmodel.CounterViewModel

open class BaseActivity : AppCompatActivity() {

    private val viewModel: CounterViewModel by viewModels()
    private lateinit var  prefs: SharedPreferences

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs =  PreferenceManager.getDefaultSharedPreferences(application)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        applyTheme()
        screenOn()
    }

    private fun screenOn() {
        if (isScreenOnEnabled()){
            ScreenManager(this).keepScreenOn(true)
        } else {
            ScreenManager(this).keepScreenOn(false)
        }
    }

    private fun isScreenOnEnabled(): Boolean = prefs.getBoolean(PreferenceKeys.PREF_SCREEN_ON, false)

    private fun applyTheme() {
        val theme = ThemeSharedPref.getTheme(this)
        when (theme) {
            THEME_PINK -> setTheme(R.style.Pink_Theme_TapCounter)
            THEME_GREEN -> setTheme(R.style.Base_Theme_TapCounter)
            THEME_BLUE -> setTheme(R.style.Blue_Theme_TapCounter)
        }
    }

    fun savePreference(theme: String) {
        ThemeSharedPref.setTheme(this, theme)
        applyTheme()
        recreate()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return when {
            viewModel.handleVolumeButton(event) -> true
            else -> super.dispatchKeyEvent(event)
        }
    }

    override fun onResume() {
        super.onResume()
        screenOn()
    }
}