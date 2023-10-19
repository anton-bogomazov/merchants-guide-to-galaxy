package com.abogomazov.merchant.guide.usecase.common

import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.roman.RomanDigit

fun interface TranslationProvider {
    fun getTranslation(digit: LocalDigit): RomanDigit?
}
