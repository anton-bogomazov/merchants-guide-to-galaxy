package com.abogomazov.merchant.guide.domain

import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import com.abogomazov.merchant.guide.translator.TranslationProvider

class EnglishTranslationProvider : TranslationProvider {

    private val dictionary = mapOf(
        one() to RomanDigit.I,
        five() to RomanDigit.V,
        ten() to RomanDigit.X,
        fifty() to RomanDigit.L,
        localDigit("hundred") to RomanDigit.C,
        localDigit("five-hundred") to RomanDigit.D,
        localDigit("thousand") to RomanDigit.M,
    )

    override fun getTranslation(digit: LocalDigit): RomanDigit {
        return dictionary[digit] ?: error("No association found for LocalDigit=${digit}")
    }

}

fun englishDictionary() = EnglishTranslationProvider()

fun localDigit(value: String) = LocalDigit(value)
fun five() = localDigit("five")
fun ten() = localDigit("ten")
fun fifty() = localDigit("fifty")
fun one() = localDigit("one")
