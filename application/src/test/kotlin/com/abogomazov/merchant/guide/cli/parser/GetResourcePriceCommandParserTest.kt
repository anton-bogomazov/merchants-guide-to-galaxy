package com.abogomazov.merchant.guide.cli.parser

import com.abogomazov.merchant.guide.cli.NullCommand
import com.abogomazov.merchant.guide.cli.NullParser
import com.abogomazov.merchant.guide.cli.commands.GetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.domain.dirt
import com.abogomazov.merchant.guide.domain.five
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumber
import com.abogomazov.merchant.guide.domain.galaxyNumeral
import com.abogomazov.merchant.guide.domain.galaxyNumber
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.one
import com.abogomazov.merchant.guide.domain.resource
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData

class GetResourcePriceCommandParserTest : FreeSpec({

    "parses whole galaxy number as a single argument" - {
        withData(
            nameFn = { it.first },
            case("how many Credits is one five Dirt ?", galaxyNumber(one(), five())),
            case("\nhow many \tCredits is one  Dirt  \n ? \n", galaxyNumber(one())),
            case("how many Credits is one five one five one five one five Dirt ? ",
                galaxyNumber(one(), five(), one(), five(), one(), five(), one(), five())),
            case(" how many      Credits   is one    Dirt  ? ", galaxyNumber(one())),
            case("     how many Credits is \n one five     one five\t one five    Dirt ?",
                galaxyNumber(one(), five(), one(), five(), one(), five())),
            case("how many Credits is one five Dirt Dirt ?",
                galaxyNumber(one(), five(), galaxyNumeral("Dirt"))),
            case("how many Credits is one five much ?", galaxyNumber(one(), five()),
                resource("much")),
        ) { (command, galaxyNumber, resource) ->
            GetResourcePriceCommandParser(NullParser).parse(command)
                .shouldBeRight(
                    GetResourceMarketPriceCommand(
                        galaxyNumber = galaxyNumber,
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

private fun case(command: String, galaxyNumber: GalaxyNumber, resource: Resource = dirt()) =
    Triple(command, galaxyNumber, resource)
