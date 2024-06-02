package com.shakilpatel.techsavvy.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun generateRandomString(charLength: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..charLength)
        .map { allowedChars.random() }
        .joinToString("")
}

fun timeAgo(timestampMillis: Long, format: String = "dd MMM yy"): String {
    val now = Date()
    val difference = ((now.time - timestampMillis) / 60000).toInt()

    return when {
        difference < 1 -> "just now"
        difference < 60 -> "$difference minute${if (difference > 1) "s" else ""} ago"
        difference < 24 * 60 -> "${difference / 60} hour${if (difference / 60 > 1) "s" else ""} ago"
        else -> getDateFromMillis(format, timestampMillis)
    }
}

fun getDateFromMillis(format: String, timestampMillis: Long): String {
    val date = Date(timestampMillis)
    val desiredFormat = SimpleDateFormat(format, Locale.getDefault())
    desiredFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
    return desiredFormat.format(date)
}