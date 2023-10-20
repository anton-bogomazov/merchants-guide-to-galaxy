package com.abogomazov.merchant.guide.application.inmemory

import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import com.abogomazov.merchant.guide.usecase.common.TranslationProvider
import com.abogomazov.merchant.guide.usecase.translator.TranslationPersister
import com.abogomazov.merchant.guide.usecase.translator.TranslationRemover


class InMemoryDictionary : TranslationProvider, TranslationPersister, TranslationRemover {

    private val dictionary = KeyValueInMemoryStorage<LocalDigit, RomanDigit>()

    override fun getTranslation(digit: LocalDigit) = dictionary.get(digit)

    override fun getTranslation(digit: RomanDigit) = dictionary.getByValue(digit)

    override fun associate(localDigit: LocalDigit, romanDigit: RomanDigit) {
        dictionary.set(localDigit, romanDigit)
    }

    override fun remove(localDigit: LocalDigit, romanDigit: RomanDigit) {
        dictionary.delete(localDigit)
    }
}
