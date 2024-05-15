package com.jay.playground.listener

import com.jay.playground.client.dto.PaymentDone
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.retry.annotation.Backoff
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger

@Service
class TestConsumerService {

    val atomicInteger = AtomicInteger()

//    @KafkaListener(topics = ["Topic2"], groupId = "test-group", containerFactory = "paymentDoneFactory")
    fun listen(paymentDoneEvent: PaymentDone) {
        println("Hi : ${atomicInteger.addAndGet(1)}")
        throw RuntimeException("예외 던지기")
    }

    @KafkaListener(topics = ["Topic2"], groupId = "test-group", containerFactory = "paymentDoneFactory")
    @RetryableTopic(backoff = Backoff(value = 3000L), attempts = "3", autoCreateTopics = "true")
    fun listenWithAsyncRetry(paymentDoneEvent: PaymentDone) {
        println("Hi : ${atomicInteger.addAndGet(1)}")
        throw RuntimeException("예외 던지기")
    }
}
