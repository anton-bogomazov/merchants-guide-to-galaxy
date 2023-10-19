package com.abogomazov.merchant.guide.application.inmemory

import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import com.abogomazov.merchant.guide.usecase.common.TranslationProvider
import com.abogomazov.merchant.guide.usecase.translator.TranslationPersister


class InMemoryTranslationRegistry : TranslationProvider, TranslationPersister {
    override fun getTranslation(digit: LocalDigit): RomanDigit {
        TODO("Not yet implemented")
    }

    override fun associate(localNum: LocalDigit, romanNum: RomanDigit) {
        TODO("Not yet implemented")
    }

}
