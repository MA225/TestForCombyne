package com.ma.testforcombyne.global

fun toDouble(string: String?) : Double {
    return if (string.isNullOrEmpty()) {
        0.0
    } else {
        string.toDouble()
    }
}