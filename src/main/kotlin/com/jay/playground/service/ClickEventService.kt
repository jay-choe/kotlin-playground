package com.jay.playground.service

import com.jay.playground.client.dto.ProductClickEvent
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class ClickEventService(
    private val redisTemplate: RedisTemplate<String, String>

) {

    fun addClickEvent(event: ProductClickEvent) {
        redisTemplate.opsForZSet().incrementScore(event.eventId, event.productId, 1.0)
    }

    fun addClickEvent(event: ProductClickEvent, score: Double) {
        redisTemplate.opsForZSet().add(event.eventId, event.productId, score)
    }

    fun retrieveProductScore(eventId: String, productId: String): Double {
        return redisTemplate.opsForZSet().score(eventId, productId)!!
    }

    fun retrieveTop10(eventId: String): Set<String> {
        return redisTemplate.opsForZSet().reverseRange(eventId, 0, 10)!!
    }

    fun delete(key: String) {
        redisTemplate.delete(key)
    }
}
