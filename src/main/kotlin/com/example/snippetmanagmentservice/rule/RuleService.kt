package com.example.snippetmanagmentservice.rule

import org.springframework.stereotype.Service

@Service
class RuleService(private val ruleRepository: RuleRepository) {
    fun getRules(): List<Rule> = ruleRepository.findAll().toList()
}