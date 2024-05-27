package com.jay.playground.client.dto

/**
 * 실시간 상품 클릭 횟수 랭킹 구현 하기
 */
data class ProductClickEvent(
    val eventId: String,
    val productId: String
)
