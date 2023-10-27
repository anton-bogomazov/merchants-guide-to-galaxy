package com.abogomazov.merchant.guide.domain

import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumber
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.market.UnitPrice
import com.abogomazov.merchant.guide.domain.roman.Amount
import io.kotest.assertions.arrow.core.shouldBeRight
import java.math.BigDecimal
import java.math.BigInteger

fun resource(value: String) =
    Resource.from(value).shouldBeRight()
fun dirt() = resource("Dirt")
fun water() = resource("Water")
fun steamDeck() = resource("SteamDeck")

fun galaxyNumeral(value: String) =
    GalaxyNumeral.from(value).shouldBeRight()
fun five() = galaxyNumeral("five")
fun ten() = galaxyNumeral("ten")
fun fifty() = galaxyNumeral("fifty")
fun one() = galaxyNumeral("one")

fun galaxyNumber(vararg digits: GalaxyNumeral) =
    GalaxyNumber.from(digits.toList()).shouldBeRight()
fun galaxyThree() = galaxyNumber(one(), one(), one())
fun galaxyFour() = galaxyNumber(one(), five())

fun amount(int: Int) = Amount.from(int).shouldBeRight()
fun price(double: Double) =
    UnitPrice.from(double.toBigDecimal()).shouldBeRight()
fun credits(int: Int) =
    Credits.from(bigInt(int)).shouldBeRight()
fun credits(bigInteger: BigInteger) =
    Credits.from(bigInteger).shouldBeRight()

fun bigInt(value: Int): BigInteger =
    BigInteger.valueOf(value.toLong())

fun BigDecimal.dropFractional() = this.toBigInteger().toBigDecimal()
