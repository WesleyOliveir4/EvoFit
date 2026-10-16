package com.guaracode.evofit.presentation.ui.feature.profile.support.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.guaracode.evofit.R

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
                question = stringResource(id = R.string.faq_q1),
                answer = stringResource(id = R.string.faq_a1)
            ),
            FAQItemData(
                id = 2,
                question = stringResource(id = R.string.faq_q2),
                answer = stringResource(id = R.string.faq_a2)
            ),
            FAQItemData(
                id = 3,
                question = stringResource(id = R.string.faq_q3),
                answer = stringResource(id = R.string.faq_a3)
            ),
            FAQItemData(
                id = 4,
                question = stringResource(id = R.string.faq_q4),
                answer = stringResource(id = R.string.faq_a4)
            ),
            FAQItemData(
                id = 5,
                question = stringResource(id = R.string.faq_q5),
                answer = stringResource(id = R.string.faq_a5)
            ),
            FAQItemData(
                id = 6,
                question = stringResource(id = R.string.faq_q6),
                answer = stringResource(id = R.string.faq_a6)
            )
        )
    }
}
