package com.example.snippetmanagmentservice.snippet

import com.example.snippetmanagmentservice.auth.IdExtractor.Companion.getId
import com.example.snippetmanagmentservice.printscript.RunnerCaller
import com.example.snippetmanagmentservice.rule.RuleService
import com.example.snippetmanagmentservice.snippet.dto.SnippetPostDTO
import com.example.snippetmanagmentservice.snippet.utils.*
import com.example.snippetmanagmentservice.userRule.UserRuleService
import input.InputStreamInput
import input.LexerInput
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/snippets")
class SnippetController(
    private val snippetService: SnippetService,
    private val userRuleService: UserRuleService,
    private val ruleService: RuleService
) {

    @PostMapping("/create")
    fun createSnippet(@RequestBody snippet: SnippetPostDTO): ResponseEntity<Snippet> {
        val createdSnippet = snippetService.saveSnippet(snippet)
        return ResponseEntity(createdSnippet, HttpStatus.CREATED)
    }

    @PutMapping("/update/snippet")
    fun updateSnippet(@RequestParam("uuid") uuid: String, @RequestBody newCode: String): ResponseEntity<Snippet> {
        val snippetUUID = UUID.fromString(uuid)
        // Verificar que el código no esté vacío
        if (newCode.isEmpty()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        val updatedSnippet = snippetService.updateSnippet(snippetUUID, newCode)
        return ResponseEntity(updatedSnippet, HttpStatus.OK)
    }

    @GetMapping("/")
    fun getSnippet(@RequestParam("uuid") uuid: String): ResponseEntity<Snippet> {
        val snippetUUID = UUID.fromString(uuid)
        val snippet = snippetService.findSnippet(snippetUUID)
        return ResponseEntity(snippet, HttpStatus.OK)
    }

    @GetMapping
    fun getSnippets(authentication: Authentication, @RequestParam("uuids") uuids: List<UUID>): ResponseEntity<List<AnalyzedSnippet>> {
        val snippets = snippetService.findSnippets(uuids)
        val analyzedSnippets = ArrayList<AnalyzedSnippet>()
        for (snippet in snippets){
            val data = getAnalyzeDataFromSnippet(snippet.id,snippetService,ruleService,userRuleService,authentication)
            analyzedSnippets.add(AnalyzedSnippet(snippet,data))
        }
        return ResponseEntity(analyzedSnippets, HttpStatus.OK)
    }

    @DeleteMapping()
    fun deleteSnippet(@RequestParam("uuid") uuid: String): ResponseEntity<Void> {
        val snippetUUID = UUID.fromString(uuid)
        snippetService.deleteSnippet(snippetUUID)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/format/snippet")
    fun formatSnippetCode(authentication: Authentication,@RequestParam("uuid") uuid: String): ResponseEntity<Any> {
        try {
            val snippetCodeFlow = getFlowCodeFromUUID(uuid, snippetService)
            val runner = RunnerCaller()
            val rules = ruleService.getRules()
            val userID = getId(authentication)
            val formattedCode = runner.formatCode(snippetCodeFlow, userRuleService.getFormattedRulesList(userID, rules))
            val updatedSnippet = snippetService.updateSnippet(UUID.fromString(uuid), formattedCode)
            return ResponseEntity(updatedSnippet, HttpStatus.OK)
        }catch (e: Exception){
            return ResponseEntity(e.message,HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping("/format/code")
    fun formatCode(authentication: Authentication,@RequestBody code: String): ResponseEntity<String> {
        try {
            val codeFlow = stringToFlow(code)
            val runner = RunnerCaller()
            val rules = ruleService.getRules()
            val userID = getId(authentication)
            val formattedCode = runner.formatCode(codeFlow, userRuleService.getFormattedRulesList(userID, rules))
            return ResponseEntity(formattedCode, HttpStatus.OK)
        } catch (e: Exception){
            return ResponseEntity(e.message,HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping("/validate")
    fun checkValidationSnippet(authentication: Authentication, @RequestParam("uuid") uuid: String): ResponseEntity<Any> {
        try {
            val snippetCodeFlow = getFlowCodeFromUUID(uuid, snippetService)
            val runner = RunnerCaller()
            val rules = ruleService.getRules()
            val userID = getId(authentication)
            val isValidCode = runner.analyzeCode(snippetCodeFlow, userRuleService.getLintedRulesList(userID, rules))
            return ResponseEntity(isValidCode, HttpStatus.OK)
        } catch (e: Exception){
            return ResponseEntity(e.message,HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping("/run")
    fun runSnippet(@RequestParam("uuid") uuid: String): ResponseEntity<Any> {
        try {
            val snippetCodeFlow = getFlowCodeFromUUID(uuid, snippetService)
            val runner = RunnerCaller()
            val messages = runner.executeCode(snippetCodeFlow)
            return ResponseEntity(messages,HttpStatus.OK)
        }catch (
            e: Exception
        ){
            return ResponseEntity(e.message,HttpStatus.BAD_REQUEST)
        }
    }

}