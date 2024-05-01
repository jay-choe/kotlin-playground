package com.jay.playground.client.config

import com.jay.playground.client.dto.PaymentDone
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaConfig {

    fun producerConfig(): Map<String, Any> {
        return mutableMapOf<String, Any>().also {
            it[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
            it[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
            it[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java.name
            it[ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG] = true
        }

    }

    @Bean
    fun kafkaProducer(): KafkaTemplate<String, PaymentDone> {
        return KafkaTemplate(DefaultKafkaProducerFactory(producerConfig()))
    }
}