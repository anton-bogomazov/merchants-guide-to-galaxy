package com.abogomazov.merchant.guide.cli.parser

import java.lang.StringBuilder

class CommandRegexBuilder {

    private val resultRegex = StringBuilder()

    companion object {
        private const val LOCAL_NUM = "(?:\\w+\\s*)+"
        private const val ARABIC_NUM = "\\d+"
        private const val ROMAN_NUM = "[MDCLXVI]"
        private const val RESOURCE = "\\w+"

        private const val SPACE = "\\s+"
        private const val OPTIONAL_SPACE = "\\s*"
        private const val QUESTION = "\\?"
    }

    fun LocalNum() = add(captured(LOCAL_NUM))
    fun ArabNum() = add(captured(ARABIC_NUM))
    fun RomanNum() = add(captured(ROMAN_NUM))
    fun Resource() = add(captured(RESOURCE))

    fun s() = add(SPACE)
    fun os() = add(OPTIONAL_SPACE)
    fun question() = add(QUESTION)

    fun how() = add("how")
    fun iz() = add("is")
    fun many() = add("many")
    fun much() = add("much")
    fun credits() = add("Credits")

    private fun add(str: String): CommandRegexBuilder {
        resultRegex.append(str)
        return this
    }

    private fun captured(seq: String) = "($seq)"

    fun build(): Regex = resultRegex.toString().toRegex()
}
