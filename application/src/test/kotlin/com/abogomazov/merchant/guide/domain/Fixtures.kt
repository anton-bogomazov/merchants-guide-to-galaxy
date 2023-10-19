package com.abogomazov.merchant.guide.domain

import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.domain.roman.Amount
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluator
import com.abogomazov.merchant.guide.usecase.translator.TranslationPersister
import com.abogomazov.merchant.guide.usecase.common.TranslationProvider
import com.abogomazov.merchant.guide.usecase.market.MarketPriceProvider
import java.math.BigDecimal
import java.math.BigInteger

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

fun englishNumberEvaluator() = LocalNumberEvaluator(englishDictionary())

class WaterDirtMarketPriceProvider : MarketPriceProvider {
    override fun getUnitPrice(resource: Resource): UnitPrice? {
        return when (resource) {
            dirt() -> UnitPrice.of(bigDec(1.5))
            water() -> UnitPrice.of(bigDec(0.9))
            else -> null
        }
    }
}

fun waterDirtMarket() = WaterDirtMarketPriceProvider()

fun dirt() = Resource("Dirt")
fun water() = Resource("Water")
fun steamDeck() = Resource("SteamDeck")

fun localDigit(value: String) = LocalDigit(value)
fun five() = localDigit("five")
fun ten() = localDigit("ten")
fun fifty() = localDigit("fifty")
fun one() = localDigit("one")

fun localNumber(vararg digits: LocalDigit) = LocalNumber(digits.toList())

fun localThree() = localNumber(one(), one(), one())
fun localFour() = localNumber(one(), five())


fun amount(int: Int) = Amount(int)
fun price(float: Float) = UnitPrice(float.toBigDecimal())
fun credits(int: Int) = Credits(bigInt(int))

fun bigInt(value: Int) = BigInteger.valueOf(value.toLong())
fun bigDec(value: Double) = BigDecimal.valueOf(value)
