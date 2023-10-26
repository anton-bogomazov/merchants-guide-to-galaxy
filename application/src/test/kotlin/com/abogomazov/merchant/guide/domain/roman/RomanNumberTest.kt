@file:Suppress("WildcardImport")

package com.abogomazov.merchant.guide.domain.roman

import com.abogomazov.merchant.guide.domain.roman.RomanNumeral.C
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral.D
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral.I
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral.L
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral.M
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral.V
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral.X
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.property.forAll

class RomanNumberTest : FreeSpec({

    "any single digit is allowed" {
        forAll<RomanNumeral> { digit ->
            RomanNumber.from(listOf(digit)).isRight()
        }
    }

    "(I,X,C,M) can't come together more than 3 times" - {
        withData(
            nameFn = { it.toString() },
            listOf(I, I, I, I),
            listOf(X, I, I, I, I, I),
            listOf(I, I, I, I, X, X),
            listOf(I, I, I, C, C, C, C, X)
        ) { digits ->
            RomanNumber.from(digits)
                .shouldBeLeft()
                .shouldBe(RomanNumberValidationError.ExceedsConsecutiveNumeralsLimit)
        }
    }

    "(V,L,D) are not repeated" - {
        withData(
            nameFn = { it.toString() },
            listOf(V, V),
            listOf(D, D, D),
            listOf(M, L, L, L)
        ) { digits ->
            RomanNumber.from(digits)
                .shouldBeLeft()
                .shouldBe(RomanNumberValidationError.RepeatedVLOrD)
        }
    }

    "Only (I,X,C) can be used as subtractive numerals" - {
        withData(
            nameFn = { it.toString() },
            listOf(L, M),
            listOf(D, M),
        ) { digits ->
            RomanNumber.from(digits)
                .shouldBeLeft()
                .shouldBe(RomanNumberValidationError.IncorrectSubtractiveNumeral)
        }
    }

    "multiple subtractive numerals are not allowed" - {
        withData(
            nameFn = { it.toString() },
            listOf(I, I, X),
            listOf(C, C, M),
        ) { digits ->
            RomanNumber.from(digits)
                .shouldBeLeft()
                .shouldBe(RomanNumberValidationError.MultipleSubtractiveNumerals)
        }
    }

    "one subtractive numeral can be used" - {
        withData(
            nameFn = { it.toString() },
            listOf(I, X),
            listOf(X, I, V),
            listOf(M, C, M),
        ) { digits ->
            RomanNumber.from(digits).shouldBeRight()
        }
    }

    "subtractive numeral should not differ more than one digit" - {
        withData(
            nameFn = { it.toString() },
            listOf(I, L),
            listOf(X, D, V),
            listOf(X, M, I),
            listOf(M, M, M, X, M, I),
        ) { digits ->
            RomanNumber.from(digits)
                .shouldBeLeft()
                .shouldBe(RomanNumberValidationError.IncorrectSubtractiveNumeral)
        }
    }

    "numerals should not follow in ascending order except subtractive" - {
        withData(
            nameFn = { it.toString() },
            listOf(X, C, X, C),
            listOf(X, C, X),
            listOf(C, M, C),
            listOf(M, M, M, C, M, C, M, C),
        ) { digits ->
            RomanNumber.from(digits)
                .shouldBeLeft()
                .shouldBe(RomanNumberValidationError.WrongNumeralsOrder)
        }
    }

    "roman number represents amount" - {
        withData(
            nameFn = { it.toString() },
            listOf(I) to 1,
            listOf(X, I, V) to 14,
            listOf(L, X, X, X, V, I, I, I) to 88,
            listOf(M, M, M, C, M, L, X, X, V, I, I, I) to 3978,
        ) { (digits, expected) ->
            RomanNumber.from(digits)
                .shouldBeRight()
                .toAmount()
                .toInt() shouldBe expected
        }
    }
})
