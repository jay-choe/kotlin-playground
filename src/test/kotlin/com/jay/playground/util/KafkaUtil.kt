package com.jay.playground.util

import com.jay.playground.client.dto.PaymentDone
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.ListOffsetsResult
import org.apache.kafka.clients.admin.OffsetSpec
import org.apache.kafka.clients.admin.RecordsToDelete
import org.apache.kafka.common.TopicPartition
import org.springframework.kafka.core.KafkaTemplate

object KafkaUtil {

    fun getMessageCount(topicName: String, kafkaTemplate: KafkaTemplate<String, PaymentDone>): Long {
        val adminClient: AdminClient =
            AdminClient.create(kafkaTemplate.producerFactory.configurationProperties)
        val partitions = getTopicPartitions(topicName, adminClient)

        return partitions.sumOf {
            val startOffset = getPartitionPerBeginOffsetMap(partitions, adminClient)[it]!!.offset()
            val endOffSet = getPartitionPerEndOffsetMap(partitions, adminClient)[it]!!.offset()

            endOffSet - startOffset
        }
    }

    fun deleteAllRecords(topicName: String, kafkaTemplate: KafkaTemplate<String, PaymentDone>) {
        val adminClient: AdminClient =
            AdminClient.create(kafkaTemplate.producerFactory.configurationProperties)
        val partitions = getTopicPartitions(topicName, adminClient)

        val partitionPerEndOffsetMap = getPartitionPerEndOffsetMap(partitions, adminClient)

        partitionPerEndOffsetMap.forEach{
            adminClient.deleteRecords(mapOf(it.key to RecordsToDelete.beforeOffset(it.value.offset()))).all().get()
        }
    }

    private fun getTopicPartitions(topicName: String, adminClient: AdminClient) : List<TopicPartition> {
        return adminClient.describeTopics(setOf(topicName))
            .allTopicNames().get()[topicName]?.partitions()!!
            .map { TopicPartition(topicName, it.partition()) }
    }

    private fun getPartitionPerBeginOffsetMap(partitions: List<TopicPartition>, adminClient: AdminClient)
    : Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> {
        val beginOffsetRequest = partitions.associateWith { OffsetSpec.earliest() }
        return adminClient.listOffsets(beginOffsetRequest).all().get()
    }

    private fun getPartitionPerEndOffsetMap(partitions: List<TopicPartition>, adminClient: AdminClient)
            : Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> {
        val endOffsetRequest = partitions.associateWith { OffsetSpec.latest() }
        return adminClient.listOffsets(endOffsetRequest).all().get()
    }

}
