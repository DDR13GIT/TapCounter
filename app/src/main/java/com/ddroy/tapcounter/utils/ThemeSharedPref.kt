package com.ddroy.tapcounter.utils

import android.content.Context
import android.content.SharedPreferences
import com.ddroy.tapcounter.constants.ThemeConstants

class ThemeSharedPref(context:Context) {
    companion object {
        private const val PREFS_NAME = "theme_prefs"
        private const val KEY_THEME = "selected_theme"
    }
    private val sharePreference: SharedPreferences = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)


    fun setTheme(theme:String){
        sharePreference.edit().putString(KEY_THEME,theme).apply()
    }

    fun getTheme():String{
      return sharePreference.getString(KEY_THEME, ThemeConstants.THEME_BLUE) ?: ThemeConstants.THEME_PINK
    }


}