package com.abogomazov.merchant.guide.usecase.translator

import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.roman.RomanDigit

fun interface TranslationProvider {
    fun getTranslation(digit: LocalDigit): RomanDigit?
}
