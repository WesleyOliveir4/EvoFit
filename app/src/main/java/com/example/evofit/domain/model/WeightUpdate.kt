package com.example.evofit.domain.model

data class WeightUpdate(
    val id: String = "",
    val weight: String,
    val date: String // Formato PT-BR: dd/MM/yyyy
)
