package com.ddroy.tapcounter.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes

fun Context.getThemColor(@AttrRes attrRes: Int): Int{
    val typeValue = TypedValue()
    val theme = theme
    theme.resolveAttribute(attrRes,typeValue,true)
    return typeValue.data
}