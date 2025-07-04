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

fun String.getDynamicTextSize(): Float{
    return when {
        length < 4 -> 150f
        length in 4..4 -> 110f
        length in 5..5 -> 90f
        length in 6..6 -> 70f
        else -> 80f
    }
}