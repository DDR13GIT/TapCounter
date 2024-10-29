package com.ddroy.tapcounter.constants
import android.content.Context

object Shar {

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