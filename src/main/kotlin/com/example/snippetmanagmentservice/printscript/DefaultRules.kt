package com.example.snippetmanagmentservice.printscript

import configuration.*
import configurationLinter.*

class DefaultRules{

    val formattingRules: ArrayList<ConfigClasses> = arrayListOf(
        LineBrakeForPrintln(3),
        SpaceIndexedForIf(5),
        SpaceBeforeColon(),
        SpaceAfterColon(),
        SpaceBeforeAssignation(),
        SpaceAfterAssignation()
    )

    val lintingRules: ArrayList<ConfigClassesLinter> = arrayListOf(
        CamelCase(),
        SnakeCase(),
        PrintNormal(),
        PrintOperations(),
        ReadInputOperations(),
        ReadInputNormal()
    )

}