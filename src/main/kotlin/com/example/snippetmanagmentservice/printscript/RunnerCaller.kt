package com.example.snippetmanagmentservice.printscript

import CommonErrorHandler
import com.example.snippetmanagmentservice.snippet.utils.AnalyzeData
import configuration.ConfigClasses
import configurationLinter.ConfigClassesLinter
import interpreterUtils.ReadInput
import interpreterUtils.ReadInputImpl
import kotlinx.coroutines.flow.Flow
import printscript.CommonPrintScriptRunner
import printscript.PrintscriptRunner
import version.getLatestVersion

class RunnerCaller{

    private val printer = PrinterCollector()
    private val readInput: ReadInput = ReadInputImpl()
    private val lastVersion = getLatestVersion()
    private val runner:PrintscriptRunner = CommonPrintScriptRunner(printer,lastVersion,readInput)

    fun formatCode(snippetCodeFlow: Flow<String>, rules: ArrayList<ConfigClasses>): String {
        return runner.runFormatting(snippetCodeFlow, rules)
    }

    fun analyzeCode(snippetCodeFlow: Flow<String>, rules: ArrayList<ConfigClassesLinter>): AnalyzeData {
        val validationString = runner.runAnalyzing(snippetCodeFlow, rules)
        println(validationString)
        return if (validationString.isEmpty()){
            AnalyzeData(true, validationString)
        }else{
            AnalyzeData(false, validationString)
        }
    }

    fun executeCode(snippetCodeFlow: Flow<String>): ArrayList<String>{
        runner.runExecution(snippetCodeFlow, CommonErrorHandler())
        return printer.getMessages()
    }

}