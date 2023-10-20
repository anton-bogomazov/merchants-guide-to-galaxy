package com.abogomazov.merchant.guide.cli

import com.abogomazov.merchant.guide.cli.parser.ExitCommandParser
import com.abogomazov.merchant.guide.cli.parser.UnknownCommandParser
import com.abogomazov.merchant.guide.cli.parser.market.GetResourceMarketPriceCommandParser
import com.abogomazov.merchant.guide.cli.parser.market.SetResourceMarketPriceCommandParser
import com.abogomazov.merchant.guide.cli.parser.translator.GetTranslationCommandParser
import com.abogomazov.merchant.guide.cli.parser.translator.SetTranslationCommandParser

class ParserFactory : CommandParserFactory {
    override fun create(command: String): CommandParser {
        return when {
            SetResourceMarketPriceCommandParser.match(command) -> {
                SetResourceMarketPriceCommandParser(command)
            }
            GetResourceMarketPriceCommandParser.match(command) -> {
                GetResourceMarketPriceCommandParser(command)
            }
            GetTranslationCommandParser.match(command) -> {
                GetTranslationCommandParser(command)
            }
            SetTranslationCommandParser.match(command) -> {
                SetTranslationCommandParser(command)
            }
            ExitCommandParser.match(command) -> {
                ExitCommandParser
            }
            else -> UnknownCommandParser
        }
    }
}
