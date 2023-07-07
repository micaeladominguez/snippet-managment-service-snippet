package com.example.snippetmanagmentservice.redis.route

import com.example.snippetmanagmentservice.redis.producer.ProductCreatedProducer
import com.example.snippetmanagmentservice.snippet.Snippet
import configurationLinter.ConfigClassesLinter
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
class StreamTestRoute @Autowired constructor(private val producer: ProductCreatedProducer) {

    suspend fun updateLinting(snippets: List<Snippet>, rules: ArrayList<ConfigClassesLinter>) {
        for (i in snippets) {
            println("i : $i")
            delay(60000) // Simulación de algún procesamiento
            producer.publishEvent(i, rules)
        }
    }
}