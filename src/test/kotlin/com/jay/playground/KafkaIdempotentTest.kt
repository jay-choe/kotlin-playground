package com.jay.playground

import com.jay.playground.client.dto.PaymentDone
import com.jay.playground.listener.IdempotentListener
import com.jay.playground.listener.IdempotentListener.Companion.handleCount
import com.jay.playground.repository.EventIdRepository
import com.jay.playground.util.KafkaUtil.deleteAllRecords
import com.jay.playground.util.KafkaUtil.getMessageCount
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.event.EventListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class KafkaIdempotentTest(
    @Autowired val kafkaTemplate: KafkaTemplate<String, PaymentDone>,
    @Autowired val eventIdRepository: EventIdRepository
) {

    final val testCount = 20
    final val concurrency = 5

    @BeforeEach
    fun flush() {
        eventIdRepository.deleteAll()
        eventIdRepository.flush()
    }

    @Test
    fun `중복 이벤트가 발행돼도 한 번만 처리된다`() {
        val newFixedThreadPool = Executors.newFixedThreadPool(concurrency)

        repeat(testCount) {
            val event = PaymentDone(UUID.randomUUID().toString() , "test", 100)

            repeat(concurrency) {
                newFixedThreadPool.submit {
                    kafkaTemplate.send(IdempotentListener.topicName, event)
                }
            }
        }

        Thread.sleep(10000L)
        Assertions.assertThat(eventIdRepository.count()).isEqualTo(testCount.toLong())
        Assertions.assertThat(handleCount).isEqualTo(testCount * concurrency.toLong())
        println("=======================================")
        println(testCount)
        println(handleCount)
        println("=======================================")
    }

}
