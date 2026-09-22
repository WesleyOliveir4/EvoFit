package com.example.evofit.presentation.ui.feature.profile.support.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.evofit.R

enum class SuggestionCategory(val titleRes: Int) {
    NEW_WORKOUT(R.string.suggestion_category_new_workout),
    NEW_EXERCISE(R.string.suggestion_category_new_exercise),
    APP_FEATURE(R.string.suggestion_category_app_feature),
    OTHER(R.string.suggestion_category_other);

    @Composable
    fun getTitle(): String = stringResource(id = titleRes)
}
