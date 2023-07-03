package com.example.snippetmanagmentservice

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStream
import org.springframework.core.io.Resource
import java.io.InputStreamReader
import javax.sql.DataSource

@Component
class DataInitializer @Autowired constructor(private val dataSource: DataSource) {
    @PostConstruct
    fun initialize() {
        try {
            extracted()
        }catch (e: Exception){
            println("ERROR : ${e.message}")
        }

    }
    private fun extracted() {
        val jdbcTemplate = JdbcTemplate(dataSource)
        val resource: Resource = ClassPathResource("data.sql")
        val inputStream: InputStream = resource.inputStream
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        println("LINES + ${bufferedReader.lines()}")
        bufferedReader.useLines { lines ->
            lines.forEach { line ->
                if (line.isNotBlank()) {
                    jdbcTemplate.update(line)
                }
            }
        }
    }
}