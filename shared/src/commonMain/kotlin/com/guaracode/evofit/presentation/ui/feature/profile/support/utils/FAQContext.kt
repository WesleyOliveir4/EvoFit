package com.guaracode.evofit.presentation.ui.feature.profile.support.utils

import androidx.compose.runtime.Composable
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.faq_a1
import evofit.shared.generated.resources.faq_a2
import evofit.shared.generated.resources.faq_a3
import evofit.shared.generated.resources.faq_a4
import evofit.shared.generated.resources.faq_a5
import evofit.shared.generated.resources.faq_a6
import evofit.shared.generated.resources.faq_q1
import evofit.shared.generated.resources.faq_q2
import evofit.shared.generated.resources.faq_q3
import evofit.shared.generated.resources.faq_q4
import evofit.shared.generated.resources.faq_q5
import evofit.shared.generated.resources.faq_q6
import org.jetbrains.compose.resources.stringResource

data class FAQItemData(
    val id: Int,
    val question: String,
    val answer: String
)

object FAQContext {
    @Composable
    fun getFAQItems(): List<FAQItemData> {
        return listOf(
            FAQItemData(
                id = 1,
                question = stringResource(Res.string.faq_q1),
                answer = stringResource(Res.string.faq_a1)
            ),
            FAQItemData(
                id = 2,
                question = stringResource(Res.string.faq_q2),
                answer = stringResource(Res.string.faq_a2)
            ),
            FAQItemData(
                id = 3,
                question = stringResource(Res.string.faq_q3),
                answer = stringResource(Res.string.faq_a3)
            ),
            FAQItemData(
                id = 4,
                question = stringResource(Res.string.faq_q4),
                answer = stringResource(Res.string.faq_a4)
            ),
            FAQItemData(
                id = 5,
                question = stringResource(Res.string.faq_q5),
                answer = stringResource(Res.string.faq_a5)
            ),
            FAQItemData(
                id = 6,
                question = stringResource(Res.string.faq_q6),
                answer = stringResource(Res.string.faq_a6)
            )
        )
    }
}
