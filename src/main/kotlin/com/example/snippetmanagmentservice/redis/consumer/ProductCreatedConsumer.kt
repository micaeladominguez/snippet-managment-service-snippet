package com.example.snippetmanagmentservice.redis.consumer

import com.example.snippetmanagmentservice.redis.RedisStreamConsumer
import interpreterUtils.ReadInput
import interpreterUtils.ReadInputImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component
import printscript.CommonPrintScriptRunner
import printscript.PrintscriptRunner
import version.getLatestVersion
import java.time.Duration

@Component
class ProductCreatedConsumer @Autowired constructor(
    redis: ReactiveRedisTemplate<String, String>,
    @Value("\${stream.key}") streamKey: String,
    @Value("\${groups.product}") groupId: String
) : RedisStreamConsumer<ProductCreated>(streamKey, groupId, redis) {

    override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, ProductCreated>> {
        return StreamReceiver.StreamReceiverOptions.builder()
            .pollTimeout(Duration.ofMillis(10000)) // Set poll rate
            .targetType(ProductCreated::class.java) // Set type to de-serialize record
            .build()
    }

    override fun onMessage(record: ObjectRecord<String, ProductCreated>) {
        println("Id: ${record.id}, Snippet: ${record.stream} Group: ${groupId}")
        val snippetCodeFlow = stringToFlow(record.value.snippet)
        val printer = PrinterCollector()
        val readInput: ReadInput = ReadInputImpl()
        val lastVersion = getLatestVersion()
        val runner: PrintscriptRunner = CommonPrintScriptRunner(printer,lastVersion,readInput)
        val analyzed = runner.runAnalyzing(snippetCodeFlow, record.value.rules)
        println("CODE ANALYZED $analyzed")
    }

}