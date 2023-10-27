package com.abogomazov.merchant.guide.domain

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary

fun Arb.Companion.amount() = arbitrary {
    amount(it.random.nextInt(0, Int.MAX_VALUE))
}

fun Arb.Companion.price() = arbitrary {
    price(it.random.nextDouble(0.00, Double.MAX_VALUE))
}
