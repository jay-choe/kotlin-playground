package com.jay.playground

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class PlaygroundApplication

fun main(args: Array<String>) {
    runApplication<PlaygroundApplication>(*args)
}
