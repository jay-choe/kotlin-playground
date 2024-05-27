package com.jay.playground.redis

import com.jay.playground.client.dto.ProductClickEvent
import com.jay.playground.service.ClickEventService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SortedSetPlayground {

    val targetEventKey = "event-1"

    @Autowired
    lateinit var service: ClickEventService

    @BeforeEach
    fun init() {
        service.delete(targetEventKey)
    }

    @Test
    fun `동일 상품의 이벤트가 100번 발생하면 스코어 값은 100이다`() {
        val productId = "product-1"
        repeat(100) {
            service.addClickEvent(ProductClickEvent(targetEventKey, productId))
        }

        Assertions.assertThat(service.retrieveProductScore(targetEventKey, productId)).isEqualTo(100.0)
    }

}
