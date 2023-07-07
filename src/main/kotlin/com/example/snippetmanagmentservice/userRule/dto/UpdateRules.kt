package com.example.snippetmanagmentservice.userRule.dto

import java.util.UUID

data class RuleUpdated(
    val ruleId: UUID,
    val value: String
)
data class UpdateRules(
    val rules: List<RuleUpdated>
)

data class UpdateRulesAndSnippets(
    val rules : List<RuleUpdated>,
    val snippetsUuid : List<UUID>
)