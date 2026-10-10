package com.guaracode.evofit.core.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateMapper {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

    fun formatDate(date: Date): String {
        return synchronized(dateFormat) {
            dateFormat.format(date)
        }
    }

    fun parseDate(dateStr: String): Date? {
        return try {
            synchronized(dateFormat) {
                dateFormat.parse(dateStr)
            }
        } catch (e: Exception) {
            null
        }
    }
}
