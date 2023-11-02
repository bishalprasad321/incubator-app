package com.bishal.incubator.utils

fun generateDefaultUserName(email: String?): String {
    var username = "@"
    for (it in email!!) {
        username += if (it == '@') {
            break
        } else if (it == '.') {
            '_'
        } else {
            it
        }
    }
    return username
}