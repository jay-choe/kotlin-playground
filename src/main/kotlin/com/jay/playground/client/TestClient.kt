package com.jay.playground.client

import com.jay.playground.client.dto.TestResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "testClient", url = "\${client.test.url}")
interface TestClient {

    @GetMapping("/")
    fun fetch(): TestResponse;
}