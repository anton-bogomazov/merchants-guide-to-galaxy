package com.abogomazov.merchant.guide.cli

import com.abogomazov.merchant.guide.cli.parser.ExitCommandParser
import com.abogomazov.merchant.guide.cli.parser.UnknownCommandParser
import com.abogomazov.merchant.guide.cli.parser.market.GetResourceMarketPriceCommandParser
import com.abogomazov.merchant.guide.cli.parser.market.SetResourceMarketPriceCommandParser
import com.abogomazov.merchant.guide.cli.parser.translator.GetTranslationCommandParser
import com.abogomazov.merchant.guide.cli.parser.translator.SetTranslationCommandParser

typealias ParserConstructor = (CommandParser) -> CommandParser

class ParserFactory : CommandParserFactory {
    inner class ParserChainBuilder {
        private val chain = mutableListOf<ParserConstructor>()

        fun next(parser: ParserConstructor): ParserChainBuilder {
            chain.add(parser)
            return this
        }

        fun terminate() =
            chain.fold(
                { UnknownCommandParser }
            ) { acc: () -> CommandParser, cons: ParserConstructor ->
                { cons(acc()) }
            }()
    }

    override fun create(): CommandParser {
        return ParserChainBuilder()
            .next { ExitCommandParser(it) }
            .next { SetResourceMarketPriceCommandParser(it) }
            .next { GetResourceMarketPriceCommandParser(it) }
            .next { GetTranslationCommandParser(it) }
            .next { SetTranslationCommandParser(it) }
            .terminate()
    }
}
