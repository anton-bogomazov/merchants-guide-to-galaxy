package com.abogomazov.merchant.guide.parser

import java.lang.StringBuilder

class CommandRegexBuilder {

    private val resultRegex = StringBuilder()

    companion object {
        private const val SPACE = "\\s+"
        private const val LOCAL_NUM = "((\\w+\\s*)+)"
        private const val ARABIC_NUM = "(\\d+)"
        private const val ROMAN_NUM = "([MDCLXVI])"
        private const val RESOURCE = "(\\w+)"
        private const val QUESTION = "\\?"
    }

    fun s() = add(SPACE)
    fun localNum() = add(LOCAL_NUM)
    fun arabNum() = add(ARABIC_NUM)
    fun romanNum() = add(ROMAN_NUM)
    fun resource() = add(RESOURCE)
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

    fun build(): Regex = resultRegex.toString().toRegex()
}
