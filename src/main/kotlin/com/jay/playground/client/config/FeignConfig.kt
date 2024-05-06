package com.jay.playground.client.config

import feign.Client
import feign.httpclient.ApacheHttpClient

import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfig {

    @Bean
    fun client() : Client {
        return ApacheHttpClient(HttpClients.custom()
            .setMaxConnTotal(100).build())
    }
}
