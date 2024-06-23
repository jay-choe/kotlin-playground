package com.jay.playground.controller

import com.jay.playground.service.TestApiCallService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
    private val apiCallService: TestApiCallService
) {

    @GetMapping("/test")
    fun test() {
        apiCallService.test()
    }
}
