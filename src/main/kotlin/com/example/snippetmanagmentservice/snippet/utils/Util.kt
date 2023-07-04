package com.example.snippetmanagmentservice.snippet.utils

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import com.example.snippetmanagmentservice.auth.IdExtractor
import com.example.snippetmanagmentservice.printscript.RunnerCaller
import com.example.snippetmanagmentservice.rule.RuleService
import com.example.snippetmanagmentservice.snippet.Snippet
import com.example.snippetmanagmentservice.snippet.SnippetService
import com.example.snippetmanagmentservice.userRule.UserRuleService
import input.InputStreamInput
import input.LexerInput
import kotlinx.coroutines.flow.Flow
import org.springframework.security.core.Authentication
import java.io.InputStream
import java.util.*


fun getFlowCodeFromUUID(id: String, service: SnippetService): Flow<String> {
    val snippetUUID = UUID.fromString(id)
    val snippetCode = service.findSnippet(snippetUUID).code
    val inputStreamCode = stringToInputStream(snippetCode)
    val input: LexerInput = InputStreamInput(inputStreamCode)
    return input.getFlow()
}

fun stringToInputStream(input: String): InputStream {
    val bytes = input.toByteArray(StandardCharsets.UTF_8)
    return ByteArrayInputStream(bytes)
}

fun stringToFlow(code: String): Flow<String>{
    val inputStreamCode = stringToInputStream(code)
    val input: LexerInput = InputStreamInput(inputStreamCode)
    return input.getFlow()
}

fun getAnalyzeDataFromSnippet(
    uuid: UUID,
    serviceSnippet: SnippetService,
    ruleService: RuleService,
    userRuleService: UserRuleService,
    authentication: Authentication,
): AnalyzeData {
    val snippetCode = serviceSnippet.findSnippet(uuid).code
    val snippetCodeFlow = stringToFlow(snippetCode)
    val runner = RunnerCaller()
    val rules = ruleService.getRules()
    val userID = IdExtractor.getId(authentication)
    return runner.analyzeCode(snippetCodeFlow, userRuleService.getLintedRulesList(userID, rules))
}

class AnalyzeData(codeOk: Boolean, errors: String){
    val isValid: Boolean = codeOk
    val linesErrors: String = errors
}

class AnalyzedSnippet(snippet: Snippet , analyzeData: AnalyzeData){
    val snippet: Snippet = snippet
    val data: AnalyzeData = analyzeData
}