package com.abogomazov.merchant.guide.cli.parser

import com.abogomazov.merchant.guide.cli.NullParser
import com.abogomazov.merchant.guide.cli.commands.SetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.domain.credits
import com.abogomazov.merchant.guide.domain.dirt
import com.abogomazov.merchant.guide.domain.localDigit
import com.abogomazov.merchant.guide.domain.localFour
import com.abogomazov.merchant.guide.domain.localNumber
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
                    SetResourceMarketPriceCommand(localFour(), dirt(), credits(34))
                )
        }
    }

    "(credits, how, many, much, is) is not reserved for local digits" - {
        withData(
            "much Silver is 34 Credits" to localNumber(localDigit("much")),
            "HOW Silver is 34 Credits" to localNumber(localDigit("HOW")),
            "how is Silver is 34 Credits" to localNumber(localDigit("how"), localDigit("is")),
            "credits is Silver is 34 Credits" to localNumber(localDigit("credits"), localDigit("is")),
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
