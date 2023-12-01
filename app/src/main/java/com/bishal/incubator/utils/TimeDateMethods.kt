package com.bishal.incubator.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class TimeDateMethods {
    fun getTimestamp() : String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ss'Z'", Locale.UK)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Calcutta")
        return simpleDateFormat.format(Date())
    }
}