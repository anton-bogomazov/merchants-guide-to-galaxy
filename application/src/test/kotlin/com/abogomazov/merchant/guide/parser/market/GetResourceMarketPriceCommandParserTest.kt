package com.abogomazov.merchant.guide.parser.market

import com.abogomazov.merchant.guide.cli.commands.GetResourceMarketPriceCommand
import com.abogomazov.merchant.guide.domain.dirt
import com.abogomazov.merchant.guide.domain.five
import com.abogomazov.merchant.guide.domain.localNumber
import com.abogomazov.merchant.guide.domain.one
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.booleans.shouldBeTrue

class GetResourceMarketPriceCommandParserTest : FreeSpec({

    "matches valid get resource market price command" - {
        withData(
            "how many Credits is glob prok Silver ?",
            "how many Credits is glob X ?",
            "how many Credits is glob prok glob prok glob prok glob prok glob prok Dirt ?",
            "how many      Credits   is glob    X  ?",
            "how many Credits is \n glob prok     glob prok\t glob prok    Dirt ?",
        ) {
            GetResourceMarketPriceCommandParser.match(it).shouldBeTrue()
        }
    }

    "parses whole local number as a single argument" - {
        withData(
            "how many Credits is one five Dirt ?" to localNumber(one(), five()),
            "\nhow many Credits is one  Dirt  \n ? \n" to localNumber(one()),
            "how many Credits is one five one five one five one five Dirt ?" to
                    localNumber(one(), five(), one(), five(), one(), five(), one(), five(),),
            "how many      Credits   is one    Dirt  ?" to localNumber(one()),
            "how many Credits is \n one five     one five\t one five    Dirt ?" to
                    localNumber(one(), five(), one(), five(), one(), five()),
        ) { (command, localNumber) ->
            GetResourceMarketPriceCommandParser(command).parse()
                .shouldBeRight(
                    GetResourceMarketPriceCommand(
                        localNum = localNumber,
                        resource = dirt()
                    )
                )
        }
    }

    "invalid inputs" - {
        withData(
            "how many Credits is one five Dirt-x ?",
            "how many Credits is Dirt ?",
        ) {
            GetResourceMarketPriceCommandParser(it).parse().shouldBeLeft()
        }
    }

})
