package com.example.snippetmanagmentservice.rule

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/rules")
class RuleController(private val ruleService: RuleService) {

    @GetMapping
    fun getRules(): ResponseEntity<List<Rule>> {
        return ResponseEntity(ruleService.getRules(), HttpStatus.OK)
    }
}