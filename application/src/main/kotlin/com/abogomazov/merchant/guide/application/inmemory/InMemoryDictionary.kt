package com.abogomazov.merchant.guide.application.inmemory

import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import com.abogomazov.merchant.guide.usecase.common.TranslationProvider
import com.abogomazov.merchant.guide.usecase.translator.TranslationPersister
import com.abogomazov.merchant.guide.usecase.translator.TranslationRemover

class InMemoryDictionary : TranslationProvider, TranslationPersister, TranslationRemover {

    private val dictionary = KeyValueInMemoryStorage<GalaxyNumeral, RomanDigit>()

    override fun getTranslation(digit: GalaxyNumeral) = dictionary.get(digit)

    override fun getTranslation(digit: RomanDigit) = dictionary.getByValue(digit)

    override fun associate(galaxyNumeral: GalaxyNumeral, romanDigit: RomanDigit) {
        dictionary.set(galaxyNumeral, romanDigit)
    }

    override fun remove(galaxyNumeral: GalaxyNumeral, romanDigit: RomanDigit) {
        dictionary.delete(galaxyNumeral)
    }
}
