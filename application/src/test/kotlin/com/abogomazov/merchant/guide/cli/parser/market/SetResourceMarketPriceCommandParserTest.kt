package com.abogomazov.merchant.guide.cli.parser.market

import com.abogomazov.merchant.guide.cli.commands.SetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.cli.parser.UnknownCommandParser
import com.abogomazov.merchant.guide.domain.credits
import com.abogomazov.merchant.guide.domain.dirt
import com.abogomazov.merchant.guide.domain.localFour
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData

class SetResourceMarketPriceCommandParserTest : FreeSpec({

    "parses valid get resource market price command" - {
        withData(
            "one five Dirt is 34 Credits",
            "one five Dirt is    34 Credits ",
        ) {
            SetResourceMarketPriceCommandParser(UnknownCommandParser).parse(it)
                .shouldBeRight(
                    SetResourceMarketPriceCommand(localFour(), dirt(), credits(34))
                )
        }
    }

})
