package com.abogomazov.merchant.guide.cli

import com.abogomazov.merchant.guide.cli.CommandParser
import com.abogomazov.merchant.guide.cli.CommandParserFactory
import com.abogomazov.merchant.guide.cli.parser.ExitCommandExtractor
import com.abogomazov.merchant.guide.cli.parser.market.GetResourceMarketPriceCommandParser
import com.abogomazov.merchant.guide.cli.parser.market.SetResourceMarketPriceCommandParser
import com.abogomazov.merchant.guide.cli.parser.translator.GetTranslationCommandParser
import com.abogomazov.merchant.guide.cli.parser.translator.SetTranslationCommandParser
import com.abogomazov.merchant.guide.cli.parser.UnknownCommandParser

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
            ExitCommandExtractor.match(command) -> {
                ExitCommandExtractor(command)
            }
            else -> UnknownCommandParser
        }
    }

}
