package com.guaracode.evofit.core.common

import kotlinx.datetime.*

/**
 * Utilitário multiplatform de parsing e formatação de data usando kotlinx-datetime.
 */
object DateMapper {

    fun formatDateUtc(millis: Long): String {
        return try {
            val instant = Instant.fromEpochMilliseconds(millis)
            val localDate = instant.toLocalDateTime(TimeZone.UTC).date
            val day = localDate.dayOfMonth.toString().padStart(2, '0')
            val month = localDate.monthNumber.toString().padStart(2, '0')
            val year = localDate.year
            "$day/$month/$year"
        } catch (e: Exception) {
            ""
        }
    }

    fun parseDateUtc(dateStr: String): Long? {
        return try {
            val parts = dateStr.split("/")
            if (parts.size != 3) return null
            val day = parts[0].toIntOrNull() ?: return null
            val month = parts[1].toIntOrNull() ?: return null
            val year = parts[2].toIntOrNull() ?: return null

            val localDate = LocalDate(year, month, day)
            localDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
        } catch (e: Exception) {
            null
        }
    }

    fun isValidDate(dateStr: String): Boolean {
        return parseDateUtc(dateStr) != null
    }

    fun calculateAge(birthDate: String): String {
        val millis = parseDateUtc(birthDate) ?: return ""
        val dobInstant = Instant.fromEpochMilliseconds(millis)
        val dobDate = dobInstant.toLocalDateTime(TimeZone.UTC).date
        
        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date

        var age = today.year - dobDate.year
        if (today.monthNumber < dobDate.monthNumber || (today.monthNumber == dobDate.monthNumber && today.dayOfMonth < dobDate.dayOfMonth)) {
            age--
        }
        return age.toString().coerceAtLeast("0")
    }
}
