package com.example.evofit.presentation.model

/**
 * Representação de UI de uma meta do usuário, já com os textos resolvidos.
 * Nunca deve carregar tipos de domínio (UserGoal) — apenas dados prontos para exibição.
 */
data class GoalUIModel(
    val id: String,
    val categoryLabel: String,
    val displayText: String
)
