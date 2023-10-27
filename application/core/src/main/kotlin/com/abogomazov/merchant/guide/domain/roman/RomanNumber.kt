package com.abogomazov.merchant.guide.domain.roman

import arrow.core.raise.either
import arrow.core.raise.ensure

sealed interface RomanNumberValidationError {
    data object ExceedsConsecutiveNumeralsLimit : RomanNumberValidationError
    data object WrongNumeralsOrder : RomanNumberValidationError
    data object RepeatedVLOrD : RomanNumberValidationError
    data object MultipleSubtractiveNumerals : RomanNumberValidationError
    data object IncorrectSubtractiveNumeral : RomanNumberValidationError
}

data class RomanNumber private constructor(
    private val digits: List<RomanNumeral>
) {

    companion object {
        fun from(digits: List<RomanNumeral>) = either {
            ensure(
                !digits.exceedsConsecutiveNumeralsLimit()
            ) { RomanNumberValidationError.ExceedsConsecutiveNumeralsLimit }
            ensure(!digits.hasIncorrectSubtractiveNumeral()) { RomanNumberValidationError.IncorrectSubtractiveNumeral }
            ensure(!digits.hasWrongNumeralsOrder()) { RomanNumberValidationError.WrongNumeralsOrder }
            ensure(!digits.hasRepeatedVLOrD()) { RomanNumberValidationError.RepeatedVLOrD }
            ensure(!digits.hasMultipleSubtractiveNumerals()) { RomanNumberValidationError.MultipleSubtractiveNumerals }

            RomanNumber(digits)
        }
    }

    fun toAmount() =
        Amount(
            digits.foldIndexed(0) { i, acc, _ ->
                val digitValue = digits[i].value

                if (i + 1 > digits.lastIndex) {
                    return@foldIndexed acc + digitValue
                } else if (digitValue < digits[i + 1].value) {
                    acc - digitValue
                } else {
                    acc + digitValue
                }
            }
        )
}

private const val MAX_CONSECUTIVE_NUMERALS = 3
private val NON_REPEATING_NUMERALS = setOf(RomanNumeral.V, RomanNumeral.L, RomanNumeral.D)
private val SUBTRACTIVE_NUMERALS = setOf(RomanNumeral.I, RomanNumeral.X, RomanNumeral.C)

private fun List<RomanNumeral>.exceedsConsecutiveNumeralsLimit() =
    this.windowed(MAX_CONSECUTIVE_NUMERALS + 1).any { it.allEqual() }

private fun List<RomanNumeral>.hasRepeatedVLOrD() =
    this.windowed(size = 2).any { it[0] in NON_REPEATING_NUMERALS && it.allEqual() }

private fun List<RomanNumeral>.hasIncorrectSubtractiveNumeral() =
    this.windowed(size = 2).any {
        val areSameOrder = { a: RomanNumeral, b: RomanNumeral ->
            @Suppress("MagicNumber")
            val oneOrderDistance = 10
            a.value / b.value <= oneOrderDistance
        }
        val (first, second) = it.toPair()
        if (first.value >= second.value) return@any false

        first !in SUBTRACTIVE_NUMERALS || !areSameOrder(second, first)
    }

private fun List<RomanNumeral>.hasMultipleSubtractiveNumerals() =
    this.windowed(size = 3).any {
        val (first, second, third) = it.toTriple()

        first.value == second.value && second.value < third.value
    }

private fun List<RomanNumeral>.hasWrongNumeralsOrder() =
    this.windowed(size = 3).any {
        val (first, second, third) = it.toTriple()
        if (first.value >= second.value) return@any false

        second.value < third.value || first.value == third.value
    }

private fun <T> List<T>.allEqual(): Boolean = this.all { it == this[0] }
private fun <T> List<T>.toPair(): Pair<T, T> = this[0] to this[1]
private fun <T> List<T>.toTriple(): Triple<T, T, T> = Triple(this[0], this[1], this[2])
