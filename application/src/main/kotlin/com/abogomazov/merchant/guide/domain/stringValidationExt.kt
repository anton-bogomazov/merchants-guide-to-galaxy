package com.abogomazov.merchant.guide.domain

fun String.singleWord() = this.split("\\s+".toRegex()).size == 1
fun String.containsOnlyLetters() = this.all { it.isLetter() }
