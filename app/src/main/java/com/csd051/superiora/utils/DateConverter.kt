package com.csd051.superiora.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateConverter {
    fun convertStringToMillis(dateFormat: String): Long {
        var date = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            date = formatter.parse(dateFormat)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date.time
    }
}