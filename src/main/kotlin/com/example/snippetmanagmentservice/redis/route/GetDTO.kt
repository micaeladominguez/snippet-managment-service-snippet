package com.example.snippetmanagmentservice.redis.route

import com.example.snippetmanagmentservice.redis.rule.Rule


data class GetDTO(
    val snippets: ArrayList<String>,
    val rules: ArrayList<Rule>
)