package com.example.evofit.domain.model

data class UserOnboardingData(
    val name: String,
    val age: String,
    val weight: String,
    val height: String,
    val goals: List<UserGoal>
)
