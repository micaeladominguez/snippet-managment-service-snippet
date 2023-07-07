package com.example.snippetmanagmentservice.redis.producer

import com.example.snippetmanagmentservice.redis.RedisStreamProducer
import com.example.snippetmanagmentservice.redis.consumer.ProductCreated
import kotlinx.coroutines.reactor.awaitSingle
import configurationLinter.ConfigClassesLinter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component

@Component
class ProductCreatedProducer @Autowired constructor(
    @Value("\${stream.key}") streamKey: String,
    redis: ReactiveRedisTemplate<String, String>
) : RedisStreamProducer(streamKey, redis) {

    suspend fun publishEvent(snippet: String, rules: ArrayList<ConfigClassesLinter> ) {
        println("Publishing on stream: $streamKey")
        val product = ProductCreated(snippet, rules)
        emit(product).awaitSingle()
    }
}