package com.guaracode.evofit.presentation.ui.feature.profile.support.utils

import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.suggestion_category_app_feature
import evofit.shared.generated.resources.suggestion_category_new_exercise
import evofit.shared.generated.resources.suggestion_category_new_workout
import evofit.shared.generated.resources.suggestion_category_other
import org.jetbrains.compose.resources.StringResource

enum class SuggestionCategory(val titleRes: StringResource) {
    NEW_WORKOUT(Res.string.suggestion_category_new_workout),
    NEW_EXERCISE(Res.string.suggestion_category_new_exercise),
    APP_FEATURE(Res.string.suggestion_category_app_feature),
    OTHER(Res.string.suggestion_category_other)
}
