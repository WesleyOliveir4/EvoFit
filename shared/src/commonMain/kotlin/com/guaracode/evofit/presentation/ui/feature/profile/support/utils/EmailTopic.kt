package com.guaracode.evofit.presentation.ui.feature.profile.support.utils

import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.email_topic_bug
import evofit.shared.generated.resources.email_topic_doubts
import evofit.shared.generated.resources.email_topic_feature
import evofit.shared.generated.resources.email_topic_feedback
import evofit.shared.generated.resources.email_topic_other
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class EmailTopic(val titleRes: StringResource) {
    FEEDBACK(Res.string.email_topic_feedback),
    BUG(Res.string.email_topic_bug),
    FEATURE_PROPOSAL(Res.string.email_topic_feature),
    GENERAL_DOUBTS(Res.string.email_topic_doubts),
    OTHER(Res.string.email_topic_other)
}
