package com.jay.playground.client.listener

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class TestConsumerService {

    @KafkaListener(topics = ["Topic2"], groupId = "test-group")
    fun listen(payload: String) {
        println("Hi $payload")
    }
}
