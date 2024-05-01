package com.jay.playground

import com.jay.playground.client.dto.PaymentDone
import com.jay.playground.util.KafkaUtil.deleteAllRecords
import com.jay.playground.util.KafkaUtil.getMessageCount
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import java.util.*

@SpringBootTest
class KafkaIdempotentTest(
    @Autowired val kafkaTemplate: KafkaTemplate<String, PaymentDone>
) {

    @Test
    fun `두 번 동일한 메세지를 발송하면 두개의 메세지가 쌓인다`() {
        val topic = "Topic2"
        val userData = PaymentDone(UUID.randomUUID().toString(), "User1", 100L)
        deleteAllRecords(topic, kafkaTemplate)

        kafkaTemplate.send(topic, userData).get()
        kafkaTemplate.send(topic, userData).get()
        Assertions.assertThat(getMessageCount(topic, kafkaTemplate)).isEqualTo(2)
    }

}
