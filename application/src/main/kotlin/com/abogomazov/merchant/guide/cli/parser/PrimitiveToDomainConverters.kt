package com.abogomazov.merchant.guide.cli.parser

import arrow.core.left
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral
import java.math.BigInteger

fun String.toGalaxyNumber() = GalaxyNumber(
    this.sanitized().split(" ").map { GalaxyNumeral(it) }
)

fun String.toGalaxyNumeral() = GalaxyNumeral(this)

fun String.toRomanNumeral() = RomanNumeral.valueOf(this)

fun String.toResource() = Resource.from(this)
        .mapLeft { ParserError.InvalidArguments }

fun String.toCredit() =
    try {
        Credits.from(BigInteger(this@toCredit))
            .mapLeft { ParserError.InvalidArguments }
    } catch (e: NumberFormatException) {
        ParserError.InvalidArguments.left()
    }

private fun String.sanitized() = this.replace("\\s+".toRegex(), " ").trim()
