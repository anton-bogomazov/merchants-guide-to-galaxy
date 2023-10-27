package com.abogomazov.merchant.guide.cli.parser

import com.abogomazov.merchant.guide.cli.NullParser
import com.abogomazov.merchant.guide.cli.commands.SetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.domain.credits
import com.abogomazov.merchant.guide.domain.dirt
import com.abogomazov.merchant.guide.domain.galaxyNumeral
import com.abogomazov.merchant.guide.domain.galaxyFour
import com.abogomazov.merchant.guide.domain.galaxyNumber
import com.abogomazov.merchant.guide.domain.resource
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData

class SetResourcePriceCommandParserTest : FreeSpec({

    "parses valid get resource market price command" - {
        withData(
            "one five Dirt is \t34 Credits",
            "one five Dirt is    34 Credits ",
            "   one five Dirt is    34 Credits ",
        ) {
            SetResourcePriceCommandParser(NullParser).parse(it)
                .shouldBeRight(
                    SetResourceMarketPriceCommand(galaxyFour(), dirt(), credits(34))
                )
        }
    }

    "(credits, how, many, much, is) is not reserved for galaxy numerals" - {
        withData(
            "much Silver is 34 Credits" to galaxyNumber(galaxyNumeral("much")),
            "HOW Silver is 34 Credits" to galaxyNumber(galaxyNumeral("HOW")),
            "how is Silver is 34 Credits" to galaxyNumber(galaxyNumeral("how"), galaxyNumeral("is")),
            "credits is Silver is 34 Credits" to galaxyNumber(galaxyNumeral("credits"), galaxyNumeral("is")),
        ) { (command, number) ->
            SetResourcePriceCommandParser(NullParser).parse(command)
                .shouldBeRight(
                    SetResourceMarketPriceCommand(
                        number,
                        resource("Silver"),
                        credits(34)
                    )
                )
        }
    }
})
