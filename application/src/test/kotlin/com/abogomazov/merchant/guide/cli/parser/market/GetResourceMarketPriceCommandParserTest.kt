package com.abogomazov.merchant.guide.cli.parser.market

import com.abogomazov.merchant.guide.cli.commands.GetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.cli.parser.GetResourcePriceCommandParser
import com.abogomazov.merchant.guide.cli.parser.UnknownCommandParser
import com.abogomazov.merchant.guide.domain.dirt
import com.abogomazov.merchant.guide.domain.five
import com.abogomazov.merchant.guide.domain.localNumber
import com.abogomazov.merchant.guide.domain.one
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData

class GetResourceMarketPriceCommandParserTest : FreeSpec({

    "parses whole local number as a single argument" - {
        withData(
            nameFn = { it.first },
            "how many Credits is one five Dirt ?" to localNumber(one(), five()),
            "\nhow many Credits is one  Dirt  \n ? \n" to localNumber(one()),
            "how many Credits is one five one five one five one five Dirt ?" to
                    localNumber(one(), five(), one(), five(), one(), five(), one(), five(),),
            "how many      Credits   is one    Dirt  ?" to localNumber(one()),
            "how many Credits is \n one five     one five\t one five    Dirt ?" to
                    localNumber(one(), five(), one(), five(), one(), five()),
        ) { (command, localNumber) ->
            GetResourcePriceCommandParser(UnknownCommandParser).parse(command)
                .shouldBeRight(
                    GetResourceMarketPriceCommand(
                        localNum = localNumber,
                        resource = dirt()
                    )
                )
        }
    }

    // TODO Implement detailed parsing test (reserve words (how, many, much, is))
})
