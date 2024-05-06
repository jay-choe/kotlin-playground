package com.jay.playground

import com.jay.playground.client.dto.PaymentDone
import com.jay.playground.listener.ConsumerGroupTestListener
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate

@SpringBootTest
class KafkaConsumerGroupTest(
    @Autowired val listener: ConsumerGroupTestListener,
    @Autowired val kafkaTemplate: KafkaTemplate<String, PaymentDone>
) {

    @Test
    fun `2개의 컨슈머 그룹은 각각의 오프셋을 갖는다`() {

        repeat(ConsumerGroupTestListener.produceCount) {
            kafkaTemplate.send(ConsumerGroupTestListener.topicName, PaymentDone("a", "b", 3))
        }

        listener.countDownLatch.await()
        Assertions.assertThat(listener.handleCount.get()).isEqualTo(ConsumerGroupTestListener.produceCount * 2)
    }
}
