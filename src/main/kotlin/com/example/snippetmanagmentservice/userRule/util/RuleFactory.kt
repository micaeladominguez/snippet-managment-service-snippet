package com.example.snippetmanagmentservice.userRule.util

import configuration.*
import configurationLinter.*

class RuleFactory {
    companion object {
        fun createRuleForFormatter(name: String, value: String): ConfigClasses? {
            return when (name) {
                "spaceBeforeColon" -> {
                    if(value == "true"){
                       return SpaceBeforeColon()
                    }else{
                        return null
                    }
                }
                "spaceAfterColon" -> {
                    if(value == "true"){
                        return SpaceAfterColon()
                    }else{
                        return null
                    }
                }
                "spaceBeforeAssignation" -> {
                    if(value == "true"){
                        return SpaceBeforeAssignation()
                    }else{
                        return null
                    }
                }
                "spaceAfterAssignation" -> {
                    if(value == "true"){
                        return SpaceAfterAssignation()
                    }else{
                        return null
                    }
                }
                "lineBreakBeforePrintln" -> {
                    return LineBrakeForPrintln(value.toInt())
                }
                "spaceIndexedForIf" -> {
                    return SpaceIndexedForIf(value.toInt())
                }
                else -> null
            }
        }

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