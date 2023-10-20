package com.abogomazov.merchant.guide.usecase.common

import com.abogomazov.merchant.guide.domain.emptyDictionary
import com.abogomazov.merchant.guide.domain.englishDictionary
import com.abogomazov.merchant.guide.domain.fifty
import com.abogomazov.merchant.guide.domain.five
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.one
import com.abogomazov.merchant.guide.domain.ten
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class LocalNumberEvaluatorTest : FreeSpec({

    "uses provided dictionary to translate to roman number" {
        val localNumber = LocalNumber(
            listOf(fifty(), ten(), ten(), ten(), five(), one(), one(), one())
        )

        LocalNumberEvaluator(englishDictionary())
            .evaluate(localNumber)
            .shouldBeRight()
            .toInt() shouldBe 88
    }

    "local number representing invalid roman number cannot be evaluated" {
        val localNumber = LocalNumber(
            listOf(one(), one(), five())
        )

        LocalNumberEvaluator(englishDictionary())
            .evaluate(localNumber)
            .shouldBeLeft(LocalNumberEvaluationError.RomanNotationRulesViolated)
    }

    "impossible to translate number with insufficient dictionary" {
        val localNumber = LocalNumber(
            listOf(one(), one(), five())
        )

        LocalNumberEvaluator(emptyDictionary())
            .evaluate(localNumber)
            .shouldBeLeft(LocalNumberEvaluationError.TranslationNotFound(one()))
    }

})
