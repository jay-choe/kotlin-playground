package com.jay.playground.listener

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

@Service
class ConsumerGroupTestListener {

    val handleCount: AtomicInteger = AtomicInteger()
    val countDownLatch = CountDownLatch(produceCount * 2)

    companion object {
        const val topicName = "group-test"
        const val produceCount = 4
    }

    @KafkaListener(groupId = "test-group-1", topics = [topicName])
    fun testGroup1(msg: String) {
        handleCount.addAndGet(1)
        countDownLatch.countDown()
    }

    @KafkaListener(groupId = "test-group-2", topics = [topicName])
    fun testGroup2(msg: String) {
        handleCount.addAndGet(1)
        countDownLatch.countDown()
    }

}
