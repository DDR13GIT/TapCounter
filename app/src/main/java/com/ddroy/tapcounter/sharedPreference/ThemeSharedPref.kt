package com.ddroy.tapcounter.sharedPreference
import android.content.Context
import com.ddroy.tapcounter.constants.ThemeConstants

object ThemeSharedPref {

    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_THEME = "selected_theme"

    fun getTheme(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_THEME, ThemeConstants.THEME_BLUE)?: ThemeConstants.THEME_PINK
    }

    fun setTheme(context: Context,theme: String){
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_THEME,theme).apply()
    }
}