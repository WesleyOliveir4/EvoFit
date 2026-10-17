package com.guaracode.evofit.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.guaracode.evofit.data.local.entities.ActiveSessionEntity
import com.guaracode.evofit.data.local.entities.ActiveSessionSetEntity

data class ActiveSessionWithSets(
    @Embedded val session: ActiveSessionEntity,
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "workoutId"
    )
    val sets: List<ActiveSessionSetEntity>
)
