package com.abogomazov.merchant.guide.application

import com.abogomazov.merchant.guide.parser.extractors.market.GetResourceMarketPriceArgExtractor
import com.abogomazov.merchant.guide.parser.extractors.market.SetResourceMarketPriceArgExtractor
import com.abogomazov.merchant.guide.parser.extractors.translator.GetTranslationArgExtractor
import com.abogomazov.merchant.guide.parser.extractors.translator.SetTranslationArgExtractor
import com.abogomazov.merchant.guide.parser.extractors.UnknownCommandExtractor

class ExtractorFactory : ArgumentExtractorFactory {
    override fun create(command: String): ArgumentExtractor {
        return when {
            SetResourceMarketPriceArgExtractor.match(command) -> {
                SetResourceMarketPriceArgExtractor(command)
            }
            GetResourceMarketPriceArgExtractor.match(command) -> {
                GetResourceMarketPriceArgExtractor(command)
            }
            GetTranslationArgExtractor.match(command) -> {
                GetTranslationArgExtractor(command)
            }
            SetTranslationArgExtractor.match(command) -> {
                SetTranslationArgExtractor(command)
            }
            else -> UnknownCommandExtractor
        }
    }

}
