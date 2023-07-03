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

        fun createRuleForLinter(name: String, value: String): List<ConfigClassesLinter> {
            return when (name) {
                "camelCaseApproved" -> {
                    if (value == "true") {
                        listOf(CamelCase())
                    } else {
                        listOf(SnakeCase())
                    }
                }
                "readInputWithOperation" -> {
                    val list = ArrayList<ConfigClassesLinter>()
                    list.add(ReadInputNormal())

                    if (value == "true") {
                        list.add(ReadInputOperations())
                    }
                    list
                }
                "printWithOperation" -> {
                    val list = ArrayList<ConfigClassesLinter>()
                    list.add(PrintNormal())

                    if (value == "true") {
                        list.add(PrintOperations())
                    }
                    list
                }
                else -> emptyList()
            }
        }
    }
}