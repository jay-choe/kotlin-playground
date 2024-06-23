package com.jay.playground.service

import com.jay.playground.client.config.ApplyRateLimit
import com.jay.playground.client.config.SupportedTimeUnit
import org.springframework.stereotype.Service

@Service
class TestApiCallService {

    @ApplyRateLimit(prefix = "test", timeUnit = SupportedTimeUnit.MINUTES, limit = 1)
    fun test(): String {
        print("Yes!!")
        return "TEST"
    }
}
