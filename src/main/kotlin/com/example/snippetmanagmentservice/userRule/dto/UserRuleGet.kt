package com.example.snippetmanagmentservice.userRule.dto

import com.example.snippetmanagmentservice.rule.TypeOfRule
import java.util.UUID

data class RulesValues(
    val ruleId: UUID,
    val ruleName: String,
    val ruleType: TypeOfRule,
    val value: String
)
data class UserRuleGet(
    val userId: String,
    val rulesValued: List<RulesValues>
)