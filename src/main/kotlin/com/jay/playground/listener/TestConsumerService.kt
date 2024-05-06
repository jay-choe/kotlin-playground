package com.jay.playground.listener

import com.jay.playground.client.dto.PaymentDone
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class TestConsumerService {

//    @KafkaListener(topics = ["Topic2"], groupId = "test-group", containerFactory = "paymentDoneFactory")
    fun listen(paymentDoneEvent: PaymentDone) {
        println(paymentDoneEvent.idempotentKey)
    }
}
