package com.abogomazov.merchant.guide.application.inmemory

import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import com.abogomazov.merchant.guide.usecase.common.TranslationProvider
import com.abogomazov.merchant.guide.usecase.translator.TranslationPersister


class InMemoryTranslationRegistry : TranslationProvider, TranslationPersister {

    private val dictionary = mutableMapOf<LocalDigit, RomanDigit>()

    override fun getTranslation(digit: LocalDigit): RomanDigit? {
        return dictionary[digit]
    }

    override fun associate(localDigit: LocalDigit, romanDigit: RomanDigit) {
        dictionary[localDigit] = romanDigit
    }

}
