package com.jay.playground.client.config

import com.jay.playground.client.dto.PaymentDone
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.util.backoff.ExponentialBackOff
import java.time.Duration

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
            this[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
            this[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java.name
            this[ConsumerConfig.GROUP_ID_CONFIG] = "test-group"
            this[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "latest"
            this[ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG] = true
        }
    }

    @Bean
    fun kafkaProducer(): KafkaTemplate<String, Any> {
        return KafkaTemplate(DefaultKafkaProducerFactory(producerConfig()))
    }

    @Bean
    fun paymentDoneFactory(): ConcurrentKafkaListenerContainerFactory<String, PaymentDone> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, PaymentDone>()

        factory.consumerFactory = DefaultKafkaConsumerFactory(
            consumerConfig(),
            StringDeserializer(),
            ErrorHandlingDeserializer(JsonDeserializer(PaymentDone::class.java))
        )

        factory.setConcurrency(3)
        factory.setCommonErrorHandler(blockingRetry())
        return factory
    }

    @Bean
    fun blockingRetry(): DefaultErrorHandler {
        val backOff = ExponentialBackOff(Duration.ofSeconds(1L).toMillis(), 2.0).apply {
            this.maxAttempts = 3
        }

        return DefaultErrorHandler(
            { _, exception -> println("Failed To Handle ${exception.message} Error For 3 times") },
            backOff)
    }


}
