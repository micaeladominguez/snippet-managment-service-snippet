package com.example.snippetmanagmentservice.snippet

import com.example.snippetmanagmentservice.printscript.RunnerCaller
import com.example.snippetmanagmentservice.snippet.dto.SnippetPostDTO
import com.example.snippetmanagmentservice.snippet.utils.stringToFlow
import configuration.ConfigClasses
import configurationLinter.ConfigClassesLinter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.collections.ArrayList

@RestController
@RequestMapping("/snippets")
class SnippetController(private val snippetService: SnippetService) {

    @PostMapping("/create")
    fun createSnippet(@RequestBody snippet: SnippetPostDTO): ResponseEntity<Snippet> {
        val createdSnippet = snippetService.saveSnippet(snippet)
        return ResponseEntity(createdSnippet, HttpStatus.CREATED)
    }

    @PutMapping("/updateSnippet/{uuid}")
    fun updateSnippet(@PathVariable uuid: UUID, @RequestBody newCode: String): ResponseEntity<Snippet> {
        // Verificar que el código no esté vacío
        if (newCode.isEmpty()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        val updatedSnippet = snippetService.updateSnippet(uuid, newCode)
        return ResponseEntity(updatedSnippet, HttpStatus.OK)
    }

    @GetMapping("/{uuid}")
    fun getSnippet(@PathVariable uuid: UUID): ResponseEntity<Snippet> {
        val snippet = snippetService.findSnippet(uuid)
        return ResponseEntity(snippet, HttpStatus.OK)
    }

    @GetMapping
    fun getSnippets(@RequestParam("uuids") uuids: List<UUID>): ResponseEntity<List<Snippet>> {
        val snippets = snippetService.findSnippets(uuids)
        return ResponseEntity(snippets, HttpStatus.OK)
    }

    @DeleteMapping("/{uuid}")
    fun deleteSnippet(@PathVariable uuid: UUID): ResponseEntity<Void> {
        snippetService.deleteSnippet(uuid)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/formatCode/{uuid}")
    fun formatSnippetCode(@PathVariable uuid: UUID, @RequestBody rules: ArrayList<ConfigClasses>): ResponseEntity<Any> {
        val snippetCode = snippetService.findSnippet(uuid).code
        val snippetCodeFlow = stringToFlow(snippetCode)
        val runner = RunnerCaller()
        val formattedCode = runner.formatCode(snippetCodeFlow, rules)
        val updatedSnippet = snippetService.updateSnippet(uuid, formattedCode)
        return ResponseEntity(updatedSnippet, HttpStatus.OK)
    }

    @PutMapping("/validate/{uuid}")
    fun checkValidationSnippet(@PathVariable uuid: UUID, @RequestBody rules: ArrayList<ConfigClassesLinter>): ResponseEntity<Boolean> {
        val snippetCode = snippetService.findSnippet(uuid).code
        val snippetCodeFlow = stringToFlow(snippetCode)
        val runner = RunnerCaller()
        val isValid = runner.analyzeCode(snippetCodeFlow, rules)
        return if (isValid){
            ResponseEntity(true, HttpStatus.OK)
        }else{
            ResponseEntity(false, HttpStatus.OK)
        }
    }

    @PutMapping("/run/{uuid}")
    fun runSnippet(@PathVariable uuid: UUID): ResponseEntity<ArrayList<String>> {
        val snippetCode = snippetService.findSnippet(uuid).code
        val snippetCodeFlow = stringToFlow(snippetCode)
        val runner = RunnerCaller()
        val messages = runner.executeCode(snippetCodeFlow)
        return ResponseEntity(messages,HttpStatus.OK)
    }

}