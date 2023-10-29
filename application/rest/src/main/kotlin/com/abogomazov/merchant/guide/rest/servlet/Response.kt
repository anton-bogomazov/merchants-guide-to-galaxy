package com.abogomazov.merchant.guide.rest.servlet

import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val result: String,
    val code: Int,
)
