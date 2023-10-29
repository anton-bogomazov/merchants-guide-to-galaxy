package com.abogomazov.merchant.guide.storage.inmemory

import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral
import com.abogomazov.merchant.guide.usecase.contracts.TranslationRepository

class TranslationInMemoryRepository : TranslationRepository {

    private val dictionary = KeyValueInMemoryStorage<GalaxyNumeral, RomanNumeral>()

    override fun getTranslation(digit: GalaxyNumeral) = dictionary.get(digit)

    override fun getTranslation(digit: RomanNumeral) = dictionary.getByValue(digit)

    override fun associate(galaxyNumeral: GalaxyNumeral, romanNumeral: RomanNumeral) {
        dictionary.set(galaxyNumeral, romanNumeral)
    }

    override fun remove(galaxyNumeral: GalaxyNumeral, romanNumeral: RomanNumeral) {
        dictionary.delete(galaxyNumeral)
    }
}
