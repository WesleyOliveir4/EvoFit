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
