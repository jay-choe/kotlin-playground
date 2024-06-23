package com.jay.playground.client.config

import java.time.LocalDateTime

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplyRateLimit(
    val prefix: String,
    val timeUnit: SupportedTimeUnit,
    val limit: Int,
)

enum class SupportedTimeUnit() {
    MINUTES {
        override fun toWindow(value: LocalDateTime): String {
            return value.minute.toString()
        }

        override fun getTimeToLive(): Int {
            return 1
        }

    },
    HOURS {
        override fun toWindow(value: LocalDateTime): String {
            return value.hour.toString()
        }

        override fun getTimeToLive(): Int {
           return 60
        }
    };

    abstract fun toWindow(value: LocalDateTime): String
    abstract fun getTimeToLive(): Int
    open fun generateKey(prefix: String): String = prefix + ":" + this.toWindow(LocalDateTime.now())
}
