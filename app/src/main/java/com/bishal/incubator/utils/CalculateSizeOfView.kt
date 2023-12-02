package com.bishal.incubator.utils

import android.content.Context

fun calculateSizeOfView(context: Context) : Int{
    val displayMetrics = context.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels
    return (dpWidth / COLUMN_COUNT)
}