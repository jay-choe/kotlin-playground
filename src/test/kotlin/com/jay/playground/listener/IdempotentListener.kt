package com.jay.playground.listener

import com.jay.playground.client.dto.PaymentDone
import com.jay.playground.repository.EventId
import com.jay.playground.repository.EventIdRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Service
class IdempotentListener(
    val eventIdRepository: EventIdRepository,
    val publisher: ApplicationEventPublisher
) {
    val lock: ReentrantLock = ReentrantLock()

    companion object {
        const val topicName = "Topic2"
        var handleCount = 0
    }

    @KafkaListener(groupId = "idempotent-listener", topics = [topicName], containerFactory = "paymentDoneFactory")
    fun saveEvent(event: PaymentDone) {
        deduplicate(UUID.fromString(event.idempotentKey))

        lock.withLock {
            handleCount++
        }

        publisher.publishEvent(IdempotentMessageHandleEvent("done"))
    }

    fun deduplicate(eventId: UUID) {
        try {
            eventIdRepository.saveAndFlush(EventId(eventId))
        } catch (e :Exception) {
            e.printStackTrace()
        }
    }

    data class IdempotentMessageHandleEvent(val message: String)
}
