package com.abogomazov.merchant.guide.usecase.common

import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral

interface TranslationProvider {
    fun getTranslation(digit: GalaxyNumeral): RomanNumeral?
    fun getTranslation(digit: RomanNumeral): GalaxyNumeral?
}
