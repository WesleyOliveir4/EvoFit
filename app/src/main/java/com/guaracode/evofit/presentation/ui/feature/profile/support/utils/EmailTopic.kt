package com.guaracode.evofit.presentation.ui.feature.profile.support.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.guaracode.evofit.R

enum class EmailTopic(val titleRes: Int) {
    FEEDBACK(R.string.email_topic_feedback),
    BUG(R.string.email_topic_bug),
    FEATURE_PROPOSAL(R.string.email_topic_feature),
    GENERAL_DOUBTS(R.string.email_topic_doubts),
    OTHER(R.string.email_topic_other);

    @Composable
    fun getTitle(): String = stringResource(id = titleRes)
}
