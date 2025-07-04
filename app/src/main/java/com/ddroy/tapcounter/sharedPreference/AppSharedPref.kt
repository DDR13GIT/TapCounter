package com.ddroy.tapcounter.sharedPreference
import android.content.Context
import com.ddroy.tapcounter.constants.ThemeConstants
import androidx.core.content.edit

object AppSharedPref {

    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_THEME = "selected_theme"
    private const val CONFETTI = "konfetti"
    private const val CONFETTI_COUNT = "konfetti_count"
    private const val CONFETTI_INITIAL_COUNT = "0"

    fun getTheme(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_THEME, ThemeConstants.THEME_GREEN)?: ThemeConstants.THEME_PINK
    }

    fun setTheme(context: Context,theme: String){
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit() { putString(KEY_THEME, theme) }
    }

    fun setConfettiCount(context: Context,count: String){
        val prefs = context.getSharedPreferences(CONFETTI, Context.MODE_PRIVATE)
        prefs.edit() { putString(CONFETTI_COUNT, count) }
    }

    fun getConfettiCount(context: Context?): String{
        if(context==null) return CONFETTI_INITIAL_COUNT
        val prefs = context.getSharedPreferences(CONFETTI, Context.MODE_PRIVATE)
        return prefs.getString(CONFETTI_COUNT, CONFETTI_INITIAL_COUNT)?: CONFETTI_INITIAL_COUNT
    }
}