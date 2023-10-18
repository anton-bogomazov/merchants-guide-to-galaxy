package com.abogomazov.merchant.guide.domain.local

import com.abogomazov.merchant.guide.domain.englishDictionary
import com.abogomazov.merchant.guide.domain.fifty
import com.abogomazov.merchant.guide.domain.five
import com.abogomazov.merchant.guide.domain.one
import com.abogomazov.merchant.guide.domain.ten
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class LocalNumberTest : FreeSpec({
    "uses provided dictionary to translate to roman number" {
        val localNumber = LocalNumber(
            listOf(fifty(), ten(), ten(), ten(), five(), one(), one(), one())
        )

        localNumber.toAmount(englishDictionary())
            .shouldBeRight()
            .toInt() shouldBe 88
    }

    "local number representing invalid roman number cannot be evaluated" {
        val localNumber = LocalNumber(
            listOf(one(), one(), five())
        )

        localNumber.toAmount(englishDictionary())
            .shouldBeLeft()
            .shouldBe(NumberIsNotFollowingRomanNotationRules)
    }
})
