package com.example.snippetmanagmentservice.redis.rule

import configurationLinter.*

class RuleFactory {
    companion object {
        fun createRuleForLinter(name: String, value: String): ConfigClassesLinter? {
            return when (name) {
                "camelCaseApproved" -> {
                    if(value == "true"){
                        return CamelCase()
                    }else{
                        return SnakeCase()
                    }
                }
                "readInputWithOperation" -> {
                    if(value == "true"){
                        return ReadInputOperations()
                    }else{
                        return ReadInputNormal()
                    }
                }
                "printWithOperation" -> {
                    if(value == "true"){
                        return PrintOperations()
                    }else{
                        return PrintNormal()
                    }
                }
                else -> null
            }
        }
    }
}