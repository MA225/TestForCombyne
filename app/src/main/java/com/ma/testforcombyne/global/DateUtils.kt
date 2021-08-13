package com.ma.testforcombyne.global

import java.text.SimpleDateFormat
import java.util.*

const val D_MMMM_YYYY			= "d MMMM yyyy"

fun convertDateFormat(orig: Any?) : String {
	if (orig is String) {
		val calendar = getCalendar(orig, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
		return SimpleDateFormat(D_MMMM_YYYY, Locale.ENGLISH).format(calendar.time)
	}
	return ""
}

fun getDateString(calendar: Calendar = Calendar.getInstance(), pattern: String = D_MMMM_YYYY) : String {
	return SimpleDateFormat(pattern, Locale.ENGLISH).format(calendar.time)
}

fun getCalendar(dateTime: String, pattern: String) : Calendar {
	val calendar = Calendar.getInstance()
	try {
		val date = SimpleDateFormat(pattern, Locale.ENGLISH).parse(dateTime)
		if (date != null) {
			calendar.time = date
		}
	} catch (e: Exception) {
		e.printStackTrace()
	}
	return calendar
}