package com.abogomazov.merchant.guide.cli.parser.utils

fun MatchResult?.getOneArgument() = getNArguments(n = 1)?.firstOrNull()
fun MatchResult?.getTwoArguments() = getNArguments(n = 2)?.toPair()
fun MatchResult?.getThreeArguments() = getNArguments(n = 3)?.toTriple()

private fun MatchResult?.getNArguments(n: Int) =
    this?.let { result ->
        result.groups.toList().subList(1, n + 1).mapNotNull { it?.value }
    }

private fun <T> List<T>.toPair(): Pair<T, T> = this[0] to this[1]
private fun <T> List<T>.toTriple(): Triple<T, T, T> = Triple(this[0], this[1], this[2])
