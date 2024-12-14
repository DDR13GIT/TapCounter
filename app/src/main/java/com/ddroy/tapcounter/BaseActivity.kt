package com.ddroy.tapcounter

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ddroy.tapcounter.constants.ThemeConstants.THEME_BLUE
import com.ddroy.tapcounter.constants.ThemeConstants.THEME_GREEN
import com.ddroy.tapcounter.constants.ThemeConstants.THEME_PINK
import com.ddroy.tapcounter.sharedPreference.ThemeSharedPref
import com.ddroy.tapcounter.viewmodel.CounterViewModel

open class BaseActivity : AppCompatActivity() {

    private val viewModel: CounterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyTheme()
    }

    private fun applyTheme() {
        val theme = ThemeSharedPref.getTheme(this)
        when (theme) {
            THEME_PINK -> setTheme(R.style.AppTheme)
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
}