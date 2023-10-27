package com.abogomazov.merchant.guide.cli.parser

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.sequence
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral
import java.math.BigInteger

fun String.toGalaxyNumber(): Either<ParserError, GalaxyNumber> =
    this.sanitized().split(" ")
        .map { GalaxyNumeral.from(it) }
        .let { l -> either { l.bindAll() } }
        .mapLeft { ParserError.InvalidArguments }
        .map { digits ->
            return GalaxyNumber.from(digits)
                .map { it }
                .mapLeft { ParserError.InvalidArguments }
        }

fun String.toGalaxyNumeral() = GalaxyNumeral.from(this)
    .mapLeft { ParserError.InvalidArguments }

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
