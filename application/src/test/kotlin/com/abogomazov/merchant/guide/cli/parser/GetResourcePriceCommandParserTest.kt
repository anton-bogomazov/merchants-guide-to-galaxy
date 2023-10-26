package com.abogomazov.merchant.guide.cli.parser

import com.abogomazov.merchant.guide.cli.NullCommand
import com.abogomazov.merchant.guide.cli.NullParser
import com.abogomazov.merchant.guide.cli.commands.GetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.domain.dirt
import com.abogomazov.merchant.guide.domain.five
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.localDigit
import com.abogomazov.merchant.guide.domain.localNumber
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.one
import com.abogomazov.merchant.guide.domain.resource
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData

class GetResourcePriceCommandParserTest : FreeSpec({

    "parses whole local number as a single argument" - {
        withData(
            nameFn = { it.first },
            case("how many Credits is one five Dirt ?", localNumber(one(), five())),
            case("\nhow many \tCredits is one  Dirt  \n ? \n", localNumber(one())),
            case("how many Credits is one five one five one five one five Dirt ? ",
                localNumber(one(), five(), one(), five(), one(), five(), one(), five())),
            case(" how many      Credits   is one    Dirt  ? ", localNumber(one())),
            case("     how many Credits is \n one five     one five\t one five    Dirt ?",
                localNumber(one(), five(), one(), five(), one(), five())),
            case("how many Credits is one five Dirt Dirt ?",
                localNumber(one(), five(), localDigit("Dirt"))),
            case("how many Credits is one five much ?", localNumber(one(), five()),
                resource("much")),
        ) { (command, localNumber, resource) ->
            GetResourcePriceCommandParser(NullParser).parse(command)
                .shouldBeRight(
                    GetResourceMarketPriceCommand(
                        localNum = localNumber,
                        resource = resource
                    )
                )
        }
    }

    "malformed input is not parsed as GetResourceMarketPriceCommand" - {
        withData(
            "? how many Credits is glob Mud ?",
            "how much Credits is glob prok Silver ?",
            "how many credits is glob prok Silver ?",
            "how many credits is is is Silver ?",
        ) {
            GetResourcePriceCommandParser(NullParser).parse(it)
                .shouldBeRight(NullCommand)
        }
    }

    "invalid resource parsed with error" - {
        withData(
            "how many Credits is one five Credits ?",
            "how many Credits is one five D1rt ?",
        ) {
            GetResourcePriceCommandParser(NullParser).parse(it)
                .shouldBeLeft(ParserError.InvalidArguments)
        }
    }
})

private fun case(command: String, localNumber: LocalNumber, resource: Resource = dirt()) =
    Triple(command, localNumber, resource)
