package com.guaracode.evofit.domain.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Clock

@Serializable
data class WorkoutDone(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val date: String = "",
    val exercisesByGroup: List<WorkoutGroup> = emptyList(),
    val time: String = "",
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)

data class WorkoutDoneHistory(
    val userId: String = "",
    val history: List<WorkoutDone> = emptyList()
)
