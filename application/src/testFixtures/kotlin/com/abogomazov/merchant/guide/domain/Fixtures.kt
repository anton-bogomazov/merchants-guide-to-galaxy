package com.abogomazov.merchant.guide.domain

import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumber
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
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import java.math.BigDecimal
import java.math.BigInteger

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

class WaterDirtMarketPriceProvider : MarketPriceProvider {
    override fun getUnitPrice(resource: Resource): UnitPrice? {
        return when (resource) {
            dirt() -> price(1.5)
            water() -> price(0.9)
            else -> null
        }
    }
}

fun waterDirtMarket() = WaterDirtMarketPriceProvider()

fun resource(value: String) = Resource.from(value).shouldBeRight()
fun dirt() = resource("Dirt")
fun water() = resource("Water")
fun steamDeck() = resource("SteamDeck")

fun galaxyNumeral(value: String): GalaxyNumeral = GalaxyNumeral.from(value).shouldBeRight()
fun five() = galaxyNumeral("five")
fun ten() = galaxyNumeral("ten")
fun fifty() = galaxyNumeral("fifty")
fun one() = galaxyNumeral("one")

fun galaxyNumber(vararg digits: GalaxyNumeral): GalaxyNumber =
    GalaxyNumber.from(digits.toList()).shouldBeRight()
fun galaxyThree() = galaxyNumber(one(), one(), one())
fun galaxyFour() = galaxyNumber(one(), five())

fun amount(int: Int): Amount = Amount.from(int).shouldBeRight()
fun price(double: Double): UnitPrice = UnitPrice.from(double.toBigDecimal()).shouldBeRight()
fun credits(int: Int): Credits = Credits.from(bigInt(int)).shouldBeRight()
fun credits(bigInteger: BigInteger): Credits = Credits.from(bigInteger).shouldBeRight()

fun bigInt(value: Int): BigInteger = BigInteger.valueOf(value.toLong())
fun bigDec(value: Double): BigDecimal = BigDecimal.valueOf(value)

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

fun BigDecimal.dropFractional() = this.toBigInteger().toBigDecimal()

fun Arb.Companion.amount() = arbitrary {
    amount(it.random.nextInt(0, Int.MAX_VALUE))
}

fun Arb.Companion.price() = arbitrary {
    price(it.random.nextDouble(0.00, Double.MAX_VALUE))
}
