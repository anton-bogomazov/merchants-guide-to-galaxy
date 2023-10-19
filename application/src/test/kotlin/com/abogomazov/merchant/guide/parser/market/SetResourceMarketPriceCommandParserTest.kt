package com.abogomazov.merchant.guide.parser.market

import com.abogomazov.merchant.guide.cli.commands.SetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.domain.credits
import com.abogomazov.merchant.guide.domain.dirt
import com.abogomazov.merchant.guide.domain.localFour
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.booleans.shouldBeTrue

class SetResourceMarketPriceCommandParserTest : FreeSpec({

    "matches valid get resource market price command" - {
        withData(
            "one five Dirt is 34 Credits",
            "one five Dirt is    34 Credits ",
        ) {
            SetResourceMarketPriceCommandParser.match(it)
                .shouldBeTrue()
        }
    }

    "parses valid get resource market price command" - {
        withData(
            "one five Dirt is 34 Credits",
            "one five Dirt is    34 Credits ",
        ) {
            SetResourceMarketPriceCommandParser(it).parse()
                .shouldBeRight(
                    SetResourceMarketPriceCommand(localFour(), dirt(), credits(34))
                )
        }
    }

    "invalid inputs" - {
        withData(
            "one Credits is    34 Credits ?",
            "one five Silver is    34 Credits ?",
        ) {
            GetResourceMarketPriceCommandParser(it).parse().shouldBeLeft()
        }
    }

})
