package com.abogomazov.merchant.guide.usecase.translator

import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral

fun interface TranslationRemover {
    fun remove(galaxyNumeral: GalaxyNumeral, romanNumeral: RomanNumeral)
}
