package com.abogomazov.merchant.guide.domain

import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import com.abogomazov.merchant.guide.usecase.translator.TranslationPersister
import com.abogomazov.merchant.guide.usecase.translator.TranslationProvider

class PreconfiguredTranslationProvider(
    private val dictionary: Map<LocalDigit, RomanDigit>
) : TranslationProvider {
    override fun getTranslation(digit: LocalDigit): RomanDigit? {
        return dictionary[digit]
    }
}

class InMemoryTranslationStorage(
    translations: List<Pair<LocalDigit, RomanDigit>>
) : TranslationPersister, TranslationProvider {

    private val dictionary = mutableMapOf<LocalDigit, RomanDigit>()

    init { translations.forEach { dictionary[it.first] = it.second } }

    override fun associate(localDigit: LocalDigit, romanDigit: RomanDigit) {
        dictionary[localDigit] = romanDigit
    }

    override fun getTranslation(digit: LocalDigit): RomanDigit? {
        return dictionary[digit]
    }

}

fun englishDictionary() = PreconfiguredTranslationProvider(
    mapOf(
        one() to RomanDigit.I,
        five() to RomanDigit.V,
        ten() to RomanDigit.X,
        fifty() to RomanDigit.L,
        localDigit("hundred") to RomanDigit.C,
        localDigit("five-hundred") to RomanDigit.D,
        localDigit("thousand") to RomanDigit.M,
    )
)

fun emptyDictionary() = PreconfiguredTranslationProvider(emptyMap())

fun inMemoryTranslationStorage(vararg translations: Pair<LocalDigit, RomanDigit>) =
    InMemoryTranslationStorage(translations.toList())

fun localDigit(value: String) = LocalDigit(value)
fun five() = localDigit("five")
fun ten() = localDigit("ten")
fun fifty() = localDigit("fifty")
fun one() = localDigit("one")
