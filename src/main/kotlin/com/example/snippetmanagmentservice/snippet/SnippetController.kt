package com.example.snippetmanagmentservice.snippet

import com.example.snippetmanagmentservice.auth.IdExtractor.Companion.getId
import com.example.snippetmanagmentservice.printscript.RunnerCaller
import com.example.snippetmanagmentservice.rule.RuleService
import com.example.snippetmanagmentservice.snippet.dto.SnippetPostDTO
import com.example.snippetmanagmentservice.snippet.utils.*
import com.example.snippetmanagmentservice.userRule.UserRuleService
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
    fun createSnippet(authentication: Authentication,@RequestBody snippet: SnippetPostDTO): ResponseEntity<Any> {
        try {
            var snippet = SnippetPostDTO(snippet.name,snippet.type,snippet.code);
            val runner = RunnerCaller()
            val rules = ruleService.getRules()
            val userID = getId(authentication)
            val isValidCode = runner.analyzeCode(stringToFlow(snippet.code), userRuleService.getLintedRulesList(userID, rules))
            var createdSnippet = snippetService.saveSnippet(snippet,isValidCode.linesErrors)
            return ResponseEntity(createdSnippet, HttpStatus.CREATED)
        }catch (e: Exception){
            return ResponseEntity(e.message,HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping("/update/snippet")
    fun updateSnippet(authentication: Authentication, @RequestParam("uuid") uuid: String, @RequestBody newCode: String): ResponseEntity<Any> {
        try{
            val snippetUUID = UUID.fromString(uuid)
            if (newCode.isEmpty()) {
                return ResponseEntity(HttpStatus.BAD_REQUEST)
            }
            val runner = RunnerCaller()
            val rules = ruleService.getRules()
            val userID = getId(authentication)
            val isValidCode = runner.analyzeCode(getFlowCodeFromUUID(uuid, snippetService), userRuleService.getLintedRulesList(userID, rules))
            val updatedSnippet = snippetService.updateSnippet(snippetUUID, newCode, isValidCode.linesErrors)
            return ResponseEntity(updatedSnippet, HttpStatus.OK)
        }catch (e: Exception){
            return ResponseEntity(e.message,HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/")
    fun getSnippet(@RequestParam("uuid") uuid: String): ResponseEntity<Any> {
        try {
            val snippetUUID = UUID.fromString(uuid)
            val snippet = snippetService.findSnippet(snippetUUID)
            return ResponseEntity(snippet, HttpStatus.OK)
        }catch (e: Exception){
            return ResponseEntity(e.message,HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping
    fun getSnippets(authentication: Authentication, @RequestParam("uuids") uuids: List<UUID>): ResponseEntity<Any> {
        try{
            val snippets = snippetService.findSnippets(uuids)
            val analyzedSnippets = ArrayList<AnalyzedSnippet>()
            for (snippet in snippets){
                analyzedSnippets.add(AnalyzedSnippet(snippet,AnalyzeData(snippet.linesFailed == "", snippet.linesFailed)))
            }
            return ResponseEntity(analyzedSnippets, HttpStatus.OK)
        }catch (e: Exception){
            return ResponseEntity(e.message,HttpStatus.BAD_REQUEST)
        }
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
            val snippet = snippetService.findSnippet(UUID.fromString(uuid))
            val updatedSnippet = snippetService.updateSnippet(UUID.fromString(uuid), formattedCode, snippet.linesFailed)
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
            val snippet = snippetService.findSnippet(UUID.fromString(uuid))
            val isValidCode = runner.analyzeCode(snippetCodeFlow, userRuleService.getLintedRulesList(userID, rules))
            snippetService.updateSnippet(UUID.fromString(uuid), snippet.code, isValidCode.linesErrors)
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