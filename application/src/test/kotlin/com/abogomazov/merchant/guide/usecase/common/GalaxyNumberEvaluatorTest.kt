package com.abogomazov.merchant.guide.usecase.common

import com.abogomazov.merchant.guide.domain.emptyDictionary
import com.abogomazov.merchant.guide.domain.englishDictionary
import com.abogomazov.merchant.guide.domain.fifty
import com.abogomazov.merchant.guide.domain.five
import com.abogomazov.merchant.guide.domain.galaxyNumber
import com.abogomazov.merchant.guide.domain.one
import com.abogomazov.merchant.guide.domain.ten
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class GalaxyNumberEvaluatorTest : FreeSpec({

    "uses provided dictionary to translate to roman number" {
        val galaxyNumber = galaxyNumber(
            fifty(),
            ten(),
            ten(),
            ten(),
            five(),
            one(),
            one(),
            one()
        )

        GalaxyNumberEvaluator(englishDictionary())
            .evaluate(galaxyNumber)
            .shouldBeRight()
            .toInt() shouldBe 88
    }

    "galaxy number representing invalid roman number cannot be evaluated" {
        val galaxyNumber = galaxyNumber(one(), one(), five())

        GalaxyNumberEvaluator(englishDictionary())
            .evaluate(galaxyNumber)
            .shouldBeLeft(GalaxyNumberEvaluationError.RomanNotationRulesViolated)
    }

    "impossible to translate number with insufficient dictionary" {
        val galaxyNumber = galaxyNumber(one(), one(), five())

        GalaxyNumberEvaluator(emptyDictionary())
            .evaluate(galaxyNumber)
            .shouldBeLeft(GalaxyNumberEvaluationError.TranslationNotFound(one()))
    }
})
