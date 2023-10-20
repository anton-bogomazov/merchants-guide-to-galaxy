package com.abogomazov.merchant.guide.domain

fun String.isNotCredits() = this.lowercase() != "credits"
fun String.singleWord() = this.split("\\s+".toRegex()).size == 1
fun String.containsOnlyLetters() = this.all { it.isLetter() }
