package com.abogomazov.merchant.guide.cli

import com.abogomazov.merchant.guide.cli.parser.ExitCommandParser
import com.abogomazov.merchant.guide.cli.parser.UnknownCommandParser
import com.abogomazov.merchant.guide.cli.parser.GetResourcePriceCommandParser
import com.abogomazov.merchant.guide.cli.parser.SetResourcePriceCommandParser
import com.abogomazov.merchant.guide.cli.parser.GetTranslationCommandParser
import com.abogomazov.merchant.guide.cli.parser.SetTranslationCommandParser

typealias ParserConstructor = (CommandParser) -> CommandParser

class ParserFactory : CommandParserFactory {
    private class ParserChainBuilder {
        private val chain = mutableListOf<ParserConstructor>()

        fun next(parser: ParserConstructor) =
            this.also { chain.add(parser) }

        fun terminate() =
            chain.fold(
                { UnknownCommandParser }
            ) { acc: () -> CommandParser,
                cons: ParserConstructor ->
                { cons(acc()) }
            }()
    }

    override fun create(): CommandParser =
        ParserChainBuilder()
            .next { ExitCommandParser(it) }
            .next { SetResourcePriceCommandParser(it) }
            .next { GetResourcePriceCommandParser(it) }
            .next { GetTranslationCommandParser(it) }
            .next { SetTranslationCommandParser(it) }
            .terminate()
}
