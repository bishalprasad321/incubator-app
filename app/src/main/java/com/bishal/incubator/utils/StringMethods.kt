package com.bishal.incubator.utils

class StringMethods {
    fun getTags(caption: String) : String {
        if (caption.indexOf("#") > 0) {
            val stringBuilder = StringBuilder()
            val charArray = caption.toCharArray()
            var foundWord = false
            for (c in charArray) {
                if (c == '#') {
                    foundWord = true
                    stringBuilder.append(c)
                } else if (foundWord) {
                    stringBuilder.append(c)
                }
                if (c == ' ') {
                    foundWord = false
                }
            }
            val s = stringBuilder.toString().replace(" ", "").replace("#", ",#")
            return s.substring(1, s.length)
        }
        return caption
    }
}