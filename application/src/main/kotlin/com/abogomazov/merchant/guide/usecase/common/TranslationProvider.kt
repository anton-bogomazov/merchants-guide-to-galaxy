package com.abogomazov.merchant.guide.usecase.common

import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.roman.RomanDigit

interface TranslationProvider {
    fun getTranslation(digit: GalaxyNumeral): RomanDigit?
    fun getTranslation(digit: RomanDigit): GalaxyNumeral?
}
