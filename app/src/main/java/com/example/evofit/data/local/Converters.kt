package com.example.evofit.data.local

import androidx.room.TypeConverter
import com.example.evofit.domain.model.WorkoutDone
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromWorkoutDoneList(value: List<WorkoutDone>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toWorkoutDoneList(value: String): List<WorkoutDone> {
        return Json.decodeFromString(value)
    }
}
