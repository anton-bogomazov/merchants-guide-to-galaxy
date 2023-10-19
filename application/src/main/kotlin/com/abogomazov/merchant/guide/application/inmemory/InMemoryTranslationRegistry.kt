package com.abogomazov.merchant.guide.application.inmemory

import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import com.abogomazov.merchant.guide.usecase.common.TranslationProvider
import com.abogomazov.merchant.guide.usecase.translator.TranslationPersister
import com.abogomazov.merchant.guide.usecase.translator.TranslationRemover


class InMemoryTranslationRegistry : TranslationProvider, TranslationPersister, TranslationRemover {

    private val dictionary = mutableMapOf<LocalDigit, RomanDigit>()

    override fun getTranslation(digit: LocalDigit): RomanDigit? {
        return dictionary[digit]
    }

    override fun getTranslation(digit: RomanDigit): LocalDigit? {
        return dictionary.entries.singleOrNull { it.value == digit }?.key
    }

    override fun associate(localDigit: LocalDigit, romanDigit: RomanDigit) {
        dictionary[localDigit] = romanDigit
    }

    override fun remove(localDigit: LocalDigit, romanDigit: RomanDigit) {
        dictionary.remove(localDigit)
    }

}
