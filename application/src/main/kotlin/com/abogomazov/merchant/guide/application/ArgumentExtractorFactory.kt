package com.abogomazov.merchant.guide.application

import com.abogomazov.merchant.guide.parser.market.GetResourceMarketPriceCommandParser
import com.abogomazov.merchant.guide.parser.market.SetResourceMarketPriceCommandParser
import com.abogomazov.merchant.guide.parser.translator.GetTranslationCommandParser
import com.abogomazov.merchant.guide.parser.translator.SetTranslationCommandParser
import com.abogomazov.merchant.guide.parser.UnknownCommandExtractor

class ExtractorFactory : CommandParserFactory {
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
            else -> UnknownCommandExtractor
        }
    }

}