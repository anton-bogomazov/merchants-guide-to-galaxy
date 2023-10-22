package com.abogomazov.merchant.guide.domain

import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.domain.roman.Amount
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import com.abogomazov.merchant.guide.usecase.common.LocalNumberEvaluator
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
    private val dictionary: Map<LocalDigit, RomanDigit>
) : TranslationProvider {
    override fun getTranslation(digit: LocalDigit): RomanDigit? {
        return dictionary[digit]
    }
    override fun getTranslation(digit: RomanDigit): LocalDigit? {
        return dictionary.entries.singleOrNull { it.value == digit }?.key
    }
}

class InMemoryTranslationStorage(
    translations: List<Pair<LocalDigit, RomanDigit>>
) : TranslationPersister, TranslationProvider, TranslationRemover {

    private val dictionary = mutableMapOf<LocalDigit, RomanDigit>()

    init { translations.forEach { dictionary[it.first] = it.second } }

    override fun associate(localDigit: LocalDigit, romanDigit: RomanDigit) {
        dictionary[localDigit] = romanDigit
    }

    override fun getTranslation(digit: LocalDigit): RomanDigit? {
        return dictionary[digit]
    }

    override fun getTranslation(digit: RomanDigit): LocalDigit? {
        return dictionary.entries.singleOrNull { it.value == digit }?.key
    }

    override fun remove(localDigit: LocalDigit, romanDigit: RomanDigit) {
        dictionary.remove(localDigit)
    }
}

fun englishDictionary() = PreconfiguredTranslationProvider(
    mapOf(
        one() to RomanDigit.I,
        five() to RomanDigit.V,
        ten() to RomanDigit.X,
        fifty() to RomanDigit.L,
        localDigit("hundred") to RomanDigit.C,
        localDigit("fiveHundred") to RomanDigit.D,
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
            dirt() -> price(1.5)
            water() -> price(0.9)
            else -> null
        }
    }
}

fun waterDirtMarket() = WaterDirtMarketPriceProvider()

fun dirt() = Resource.from("Dirt").shouldBeRight()
fun water() = Resource.from("Water").shouldBeRight()
fun steamDeck() = Resource.from("SteamDeck").shouldBeRight()

fun localDigit(value: String): LocalDigit = LocalDigit.from(value).shouldBeRight()
fun five() = localDigit("five")
fun ten() = localDigit("ten")
fun fifty() = localDigit("fifty")
fun one() = localDigit("one")

fun localNumber(vararg digits: LocalDigit): LocalNumber = LocalNumber.from(digits.toList()).shouldBeRight()
fun localThree() = localNumber(one(), one(), one())
fun localFour() = localNumber(one(), five())

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
