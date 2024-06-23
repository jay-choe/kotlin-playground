package com.jay.playground.client.config


import io.lettuce.core.RedisCommandExecutionException
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.DefaultRedisScript
import org.springframework.scripting.support.ResourceScriptSource
import org.springframework.stereotype.Component

@Component
@Aspect
class RateLimitAspect(
    private val redisTemplate: RedisTemplate<String, String>
) {

    private val rateLimitScript = DefaultRedisScript<Long>()

    init {
        val resource = ClassPathResource("ratelimit.lua")
        rateLimitScript.setScriptSource(ResourceScriptSource(resource))
        rateLimitScript.resultType = Long::class.java
    }


    @Around("@annotation(com.jay.playground.client.config.ApplyRateLimit)")
    fun applyRateLimit(joinPoint: ProceedingJoinPoint): Any {
        val methodSignature = joinPoint.signature as MethodSignature
        val annotation = methodSignature.method.getAnnotation(ApplyRateLimit::class.java)

        val key = listOf(annotation.timeUnit.generateKey(annotation.prefix))
        val limit = annotation.limit
        val ttl = annotation.timeUnit.getTimeToLive()

        try {
            redisTemplate.execute(rateLimitScript, key, limit.toString(), ttl.toString())
            return joinPoint.proceed()
        } catch (e: RedisCommandExecutionException) {
            e.printStackTrace() // 여기서 사용자 정의 Exception 띄우고 429 응답을 주는 방식으로 처리
            throw e
        }
    }
}
