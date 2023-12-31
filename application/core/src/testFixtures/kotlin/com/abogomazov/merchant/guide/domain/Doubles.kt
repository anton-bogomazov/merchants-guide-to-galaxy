package com.abogomazov.merchant.guide.domain

import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.domain.roman.Amount
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral
import com.abogomazov.merchant.guide.usecase.common.GalaxyNumberEvaluator
import com.abogomazov.merchant.guide.usecase.common.TranslationProvider
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceError
import com.abogomazov.merchant.guide.usecase.market.MarketPriceProvider
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceError
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationError
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationError
import com.abogomazov.merchant.guide.usecase.translator.TranslationPersister
import com.abogomazov.merchant.guide.usecase.translator.TranslationRemover

class PreconfiguredTranslationProvider(
    private val dictionary: Map<GalaxyNumeral, RomanNumeral>
) : TranslationProvider {
    override fun getTranslation(digit: GalaxyNumeral): RomanNumeral? {
        return dictionary[digit]
    }
    override fun getTranslation(digit: RomanNumeral): GalaxyNumeral? {
        return dictionary.entries.singleOrNull { it.value == digit }?.key
    }
}

class InMemoryTranslationStorage(
    translations: List<Pair<GalaxyNumeral, RomanNumeral>>
) : TranslationPersister, TranslationProvider, TranslationRemover {

    private val dictionary = mutableMapOf<GalaxyNumeral, RomanNumeral>()

    init { translations.forEach { dictionary[it.first] = it.second } }

    override fun associate(galaxyNumeral: GalaxyNumeral, romanNumeral: RomanNumeral) {
        dictionary[galaxyNumeral] = romanNumeral
    }
    override fun getTranslation(digit: GalaxyNumeral): RomanNumeral? {
        return dictionary[digit]
    }
    override fun getTranslation(digit: RomanNumeral): GalaxyNumeral? {
        return dictionary.entries.singleOrNull { it.value == digit }?.key
    }
    override fun remove(galaxyNumeral: GalaxyNumeral, romanNumeral: RomanNumeral) {
        dictionary.remove(galaxyNumeral)
    }
}

class WaterDirtMarketPriceProvider : MarketPriceProvider {
    override fun getUnitPrice(resource: Resource): UnitPrice? {
        return when (resource) {
            dirt() -> price(1.5)
            water() -> price(0.9)
            else -> null
        }
    }
}

fun englishDictionary() = PreconfiguredTranslationProvider(
    mapOf(
        one() to RomanNumeral.I,
        five() to RomanNumeral.V,
        ten() to RomanNumeral.X,
        fifty() to RomanNumeral.L,
        galaxyNumeral("hundred") to RomanNumeral.C,
        galaxyNumeral("fiveHundred") to RomanNumeral.D,
        galaxyNumeral("thousand") to RomanNumeral.M,
    )
)

fun emptyDictionary() = PreconfiguredTranslationProvider(emptyMap())

fun inMemoryTranslationStorage(vararg translations: Pair<GalaxyNumeral, RomanNumeral>) =
    InMemoryTranslationStorage(translations.toList())

fun englishNumberEvaluator() = GalaxyNumberEvaluator(englishDictionary())
fun waterDirtMarket() = WaterDirtMarketPriceProvider()

fun getResourceResult(credits: Credits) =
    either<GetResourceMarketPriceError, Credits> { return credits.right() }
fun getResourceError(error: GetResourceMarketPriceError) =
    either<GetResourceMarketPriceError, Credits> { return error.left() }

fun getTranslationResult(amount: Amount) =
    either<GetTranslationError, Amount> { return amount.right() }
fun getTranslationError(error: GetTranslationError) =
    either<GetTranslationError, Amount> { return error.left() }

fun setResourceResult() =
    either<SetResourceMarketPriceError, Unit> { return Unit.right() }
fun setResourceError(error: SetResourceMarketPriceError) =
    either<SetResourceMarketPriceError, Unit> { return error.left() }

fun setTranslationResult() =
    either<SetTranslationError, Unit> { return Unit.right() }
fun setTranslationError(error: SetTranslationError) =
    either<SetTranslationError, Unit> { return error.left() }
