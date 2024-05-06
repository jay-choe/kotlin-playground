package com.jay.playground.controller

import com.jay.playground.client.dto.PaymentDone
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class KafkaMessageSendController(
    val kafkaTemplate: KafkaTemplate<String, PaymentDone>
) {
    val topic = "Topic2"

    @PostMapping("/payment/{userId}")
    fun paymentRequest(@PathVariable userId: String) {
        val idempotentKey = UUID.randomUUID().toString()
        val amount = 100L

        kafkaTemplate.send(topic, PaymentDone(idempotentKey, userId, amount))
    }
}
