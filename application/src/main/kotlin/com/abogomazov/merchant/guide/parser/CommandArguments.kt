package com.abogomazov.merchant.guide.parser

import arrow.core.Either
import com.abogomazov.merchant.guide.cli.commands.BusinessCommand

fun interface CommandArguments {
    fun toCommand(): Either<ParserError.InvalidArguments, BusinessCommand>
}
