package com.jay.playground.client.dto

data class PaymentDone(
    val idempotentKey: String,
    val userId: String,
    val amount: Long,
)
