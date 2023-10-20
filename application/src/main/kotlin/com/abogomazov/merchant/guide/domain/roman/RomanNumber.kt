package com.abogomazov.merchant.guide.domain.roman

import arrow.core.raise.either
import arrow.core.raise.ensure
import com.abogomazov.merchant.guide.domain.roman.RomanNumberValidationError.*

sealed interface RomanNumberValidationError {
    data object ExceedsConsecutiveNumeralsLimit : RomanNumberValidationError
    data object WrongNumeralsOrder : RomanNumberValidationError
    data object RepeatedVLOrD : RomanNumberValidationError
    data object MultipleSubtractiveNumerals : RomanNumberValidationError
    data object IncorrectSubtractiveNumeral : RomanNumberValidationError
}

data class RomanNumber private constructor(
    private val digits: List<RomanDigit>
) {

    companion object {
        fun from(digits: List<RomanDigit>) = either {
            ensure(!digits.exceedsConsecutiveNumeralsLimit()) { ExceedsConsecutiveNumeralsLimit }
            ensure(!digits.hasIncorrectSubtractiveNumeral()) { IncorrectSubtractiveNumeral }
            ensure(!digits.hasWrongNumeralsOrder()) { WrongNumeralsOrder }
            ensure(!digits.hasRepeatedVLOrD()) { RepeatedVLOrD }
            ensure(!digits.hasMultipleSubtractiveNumerals()) { MultipleSubtractiveNumerals }

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
private val NON_REPEATING_NUMERALS = setOf(RomanDigit.V, RomanDigit.L, RomanDigit.D)
private val SUBTRACTIVE_NUMERALS = setOf(RomanDigit.I, RomanDigit.X, RomanDigit.C)

private fun List<RomanDigit>.exceedsConsecutiveNumeralsLimit() =
    this.windowed(MAX_CONSECUTIVE_NUMERALS + 1).any { it.allEqual() }

private fun List<RomanDigit>.hasRepeatedVLOrD() =
    this.windowed(2).any { it[0] in NON_REPEATING_NUMERALS && it.allEqual() }

private fun List<RomanDigit>.hasIncorrectSubtractiveNumeral() =
    this.windowed(2).any {
        val (first, second) = it.toPair()
        if (first.value >= second.value) return@any false

        first !in SUBTRACTIVE_NUMERALS || second.value / first.value > 10
    }

private fun List<RomanDigit>.hasMultipleSubtractiveNumerals() =
    this.windowed(3).any {
        val (first, second, third) = it.toTriple()

        first.value == second.value && second.value < third.value
    }

private fun List<RomanDigit>.hasWrongNumeralsOrder() =
    this.windowed(3).any {
        val (first, second, third) = it.toTriple()
        if (first.value >= second.value) return@any false

        second.value < third.value || first.value == third.value
    }


private fun <T> List<T>.allEqual(): Boolean = this.all { it == this[0] }
private fun <T> List<T>.toPair(): Pair<T, T> = this[0] to this[1]
private fun <T> List<T>.toTriple(): Triple<T, T, T> = Triple(this[0], this[1], this[2])
