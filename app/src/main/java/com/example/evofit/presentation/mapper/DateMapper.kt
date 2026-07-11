package com.example.evofit.presentation.mapper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateMapper {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("pt-BR"))

    fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }

    fun parseDate(dateStr: String): Date? {
        return try {
            dateFormat.parse(dateStr)
        } catch (e: Exception) {
            null
        }
    }
}
