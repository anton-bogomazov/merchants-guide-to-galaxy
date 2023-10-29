package com.abogomazov.merchant.guide.storage.postgres.repository

import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.galaxy.toGalaxyNumeral
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral
import com.abogomazov.merchant.guide.domain.roman.toRomanNumeral
import com.abogomazov.merchant.guide.storage.postgres.PostgresDatasource
import com.abogomazov.merchant.guide.usecase.common.TranslationProvider
import com.abogomazov.merchant.guide.usecase.translator.TranslationPersister
import com.abogomazov.merchant.guide.usecase.translator.TranslationRemover

class TranslationRepository(
    private val dataSource: PostgresDatasource
) : TranslationRemover, TranslationPersister, TranslationProvider {

    companion object {
        private const val TABLE = "translations"

        private const val ROMAN_NUMERAL = "roman_numeral"
        private const val GALAXY_NUMERAL = "galaxy_numeral"
    }

    override fun getTranslation(digit: GalaxyNumeral): RomanNumeral? {
        val query = "SELECT * FROM $TABLE WHERE $GALAXY_NUMERAL = '$digit'"
        val result = dataSource.single(query) { it.getString(ROMAN_NUMERAL) }

        return result?.toRomanNumeral()
    }

    override fun getTranslation(digit: RomanNumeral): GalaxyNumeral? {
        val query = "SELECT * FROM $TABLE WHERE $ROMAN_NUMERAL = '$digit'"
        val result = dataSource.single(query) { it.getString(GALAXY_NUMERAL) }

        return result?.toGalaxyNumeral()?.fold(
            { error("Database in inconsistent state! Conversion error from $result") },
            { it }
        )
    }

    override fun associate(galaxyNumeral: GalaxyNumeral, romanNumeral: RomanNumeral) {
        val query = "INSERT INTO $TABLE ($ROMAN_NUMERAL, $GALAXY_NUMERAL)\n" +
                "VALUES ('$romanNumeral', '$galaxyNumeral');"
        dataSource.update(query)
    }

    override fun remove(galaxyNumeral: GalaxyNumeral, romanNumeral: RomanNumeral) {
        val query = "DELETE FROM $TABLE\n" +
                "WHERE $GALAXY_NUMERAL = '$galaxyNumeral' AND $ROMAN_NUMERAL = '$romanNumeral';"
        dataSource.update(query)
    }
}
