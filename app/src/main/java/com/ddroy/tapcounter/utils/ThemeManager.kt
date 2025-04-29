package com.ddroy.tapcounter.utils

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.ddroy.tapcounter.R

enum class ColorTheme(val themeName: String) {
    GREEN("Green"),
    BLUE("Blue"),
//    YELLOW("Yellow"),
//    PURPLE("Purple")
}

object ThemeManager {
    private const val PREF_COLOR_THEME = "pref_color_theme"

    fun applyTheme(context: Context, theme: ColorTheme) {
        val nightMode = AppCompatDelegate.getDefaultNightMode()
        val themeResId = getThemeResId(theme, nightMode == AppCompatDelegate.MODE_NIGHT_YES)

        context.setTheme(themeResId)
        saveThemePreference(context, theme)
    }

    fun getThemeResId(theme: ColorTheme, isNight: Boolean): Int {
        return when (theme) {
            ColorTheme.GREEN -> if (isNight) R.style.theme_green else R.style.theme_green
            ColorTheme.BLUE -> if (isNight) R.style.theme_blue else R.style.theme_blue
        }
    }

    fun getSavedTheme(context: Context): ColorTheme {
        val prefs = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val themeName = prefs.getString(PREF_COLOR_THEME, ColorTheme.GREEN.name)
        return ColorTheme.valueOf(themeName ?: ColorTheme.GREEN.name)
    }

    private fun saveThemePreference(context: Context, theme: ColorTheme) {
        val prefs = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        prefs.edit().putString(PREF_COLOR_THEME, theme.name).apply()
    }

    fun applyThemeToActivity(activity: Activity) {
        val savedTheme = getSavedTheme(activity)
        applyTheme(activity, savedTheme)
        ActivityCompat.recreate(activity)
    }
}