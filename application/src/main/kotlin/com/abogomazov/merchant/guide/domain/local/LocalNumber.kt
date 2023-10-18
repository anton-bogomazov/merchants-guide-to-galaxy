package com.abogomazov.merchant.guide.domain.local

import com.abogomazov.merchant.guide.domain.roman.RomanNumber
import com.abogomazov.merchant.guide.translator.TranslationProvider

data class LocalNumber(
    private val digits: List<LocalDigit>
) {

    fun toAmount(translationProvider: TranslationProvider) =
        RomanNumber
            .from(digits.map { translationProvider.getTranslation(it) })
            .mapLeft { NumberIsNotFollowingRomanNotationRules }
            .map { it.toAmount() }

}

object NumberIsNotFollowingRomanNotationRules
