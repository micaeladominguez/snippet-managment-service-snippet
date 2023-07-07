package com.example.snippetmanagmentservice.redis.route

import com.example.snippetmanagmentservice.redis.producer.ProductCreatedProducer
import com.example.snippetmanagmentservice.redis.rule.LintedRules.Companion.getLintedRules
import configurationLinter.ConfigClassesLinter
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class StreamTestRoute @Autowired constructor(private val producer: ProductCreatedProducer) {

    @PostMapping("/linting")
    suspend fun post( @RequestBody getDTO: GetDTO) : String{
        val lintedRules = getLintedRules(getDTO.rules)

        val job = GlobalScope.async {updateLinting(getDTO.snippets, lintedRules)}

        return "Devolvio joyus"
    }


    suspend fun updateLinting(snippets: ArrayList<String>, rules: ArrayList<ConfigClassesLinter>) {
        for (i in snippets) {
            println("i : $i")
            delay(60000) // Simulación de algún procesamiento
            producer.publishEvent(i, rules)
        }
    }
}