package com.jay.playground.client.config

import com.jay.playground.client.dto.PaymentDone
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaConfig {

    private val bootStrapServerLocation = "localhost:9092"

    fun producerConfig(): Map<String, Any> {

        return mutableMapOf<String, Any>().apply {
            this[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootStrapServerLocation
            this[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
            this[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java.name
            this[ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG] = true
        }

    }

    fun consumerConfig(): Map<String, Any> {
        return mutableMapOf<String, Any>().apply {
            this[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootStrapServerLocation
            this[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
            this[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java.name
            this[ConsumerConfig.GROUP_ID_CONFIG] = "test-group"
            this[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "latest"
            this[ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG] = true
        }
    }

    @Bean
    fun kafkaProducer(): KafkaTemplate<String, PaymentDone> {
        return KafkaTemplate(DefaultKafkaProducerFactory(producerConfig()))
    }

    @Bean
    fun kafkaConsumer(): ConcurrentKafkaListenerContainerFactory<String, Any> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
        factory.consumerFactory = DefaultKafkaConsumerFactory(consumerConfig())
        factory.setConcurrency(3)

        return factory
    }
}