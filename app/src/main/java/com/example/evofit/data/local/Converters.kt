package com.example.evofit.data.local

import androidx.room.TypeConverter
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.model.WorkoutExercise
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

import com.example.evofit.domain.model.WorkoutGroup

class Converters {
    @TypeConverter
    fun fromWorkoutDoneList(value: List<WorkoutDone>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toWorkoutDoneList(value: String): List<WorkoutDone> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromWorkoutGroupList(value: List<WorkoutGroup>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toWorkoutGroupList(value: String): List<WorkoutGroup> {
        return Json.decodeFromString(value)
    }
}
