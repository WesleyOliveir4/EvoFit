package com.example.evofit.core.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utilitário puro de parsing/formatação de data (sem dependência de Android ou Compose).
 * Fica em core.common propositalmente: é usado tanto pela camada de dados (filtragem por
 * período) quanto pela camada de apresentação (exibição), e nenhuma das duas deveria
 * depender da outra para isso.
 */
object DateMapper {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    private val utcDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).apply {
        timeZone = java.util.TimeZone.getTimeZone("UTC")
    }

    fun formatDate(date: Date): String {
        return synchronized(dateFormat) {
            dateFormat.format(date)
        }
    }

    fun formatDateUtc(millis: Long): String {
        return synchronized(utcDateFormat) {
            utcDateFormat.format(Date(millis))
        }
    }

    fun parseDate(dateStr: String): Date? {
        return try {
            synchronized(dateFormat) {
                dateFormat.parse(dateStr)
            }
        } catch (e: Exception) {
            android.util.Log.e("DateMapper", "Erro ao fazer parse da data: $dateStr", e)
            null
        }
    }

    fun parseDateUtc(dateStr: String): Long? {
        return try {
            synchronized(utcDateFormat) {
                utcDateFormat.parse(dateStr)?.time
            }
        } catch (e: Exception) {
            android.util.Log.e("DateMapper", "Erro ao fazer parse da data UTC: $dateStr", e)
            null
        }
    }

    fun isValidDate(dateStr: String): Boolean {
        return parseDateUtc(dateStr) != null
    }

    fun calculateAge(birthDate: String): String {
        val millis = parseDateUtc(birthDate) ?: return ""
        val today = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
        val dob = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC")).apply { 
            timeInMillis = millis 
        }

        var age = today.get(java.util.Calendar.YEAR) - dob.get(java.util.Calendar.YEAR)
        if (today.get(java.util.Calendar.DAY_OF_YEAR) < dob.get(java.util.Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age.toString().coerceAtLeast("0")
    }
}
