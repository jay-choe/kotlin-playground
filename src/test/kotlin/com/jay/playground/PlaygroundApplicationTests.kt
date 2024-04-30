package com.jay.playground

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.jay.playground.client.TestClient
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = ["client.test.url = http://localhost:\${wiremock.server.port}"])
class PlaygroundApplicationTests(
    @Autowired
    val client: TestClient
) {

    @Test
    fun server_returns_invalid_header() {

        stubFor(
            get(urlEqualTo("/"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"element\": \"Hello, Mock\"}")
                )
        )
        print(client.fetch())
    }
}
