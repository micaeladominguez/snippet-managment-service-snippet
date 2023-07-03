package com.example.snippetmanagmentservice.snippet

import printscript.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

@RestController
@RequestMapping("/snippets")
class SnippetController(private val snippetService: SnippetService) {

    @PostMapping("/create")
    fun createSnippet(@RequestBody snippet: Snippet): ResponseEntity<Snippet> {
        // Validar que el nombre no esté vacío
        if (snippet.name.isEmpty()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        // Validar que el tipo no esté vacío
        if (snippet.type.isEmpty()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        // Validar que el código no esté vacío
        if (snippet.code.isEmpty()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
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

    @PutMapping("/formatSnippetCode/{uuid}")
    fun formatSnippetCode(@PathVariable uuid: UUID, @PathVariable configFile: File): ResponseEntity<Any> {
        val snippetCode = getSnippet(uuid).body?.code
        if (snippetCode != null){
            val snippetCodeFlow = stringToFlow(snippetCode)
            //val runner: PrintscriptRunner = CommonPrintScriptRunner()
           // val formattedCode = runner.runFormatting(snippetCodeFlow, configFile)
            //val updatedSnippet = snippetService.updateSnippet(uuid, formattedCode)
            return ResponseEntity(snippetCodeFlow, HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

//    @PutMapping("//{uuid}")
//    fun checkValidationSnippet(@PathVariable uuid: UUID, @PathVariable configFile: File): ResponseEntity<Snippet> {
//        val snippetCode = getSnippet(uuid).body?.code
//        if (snippetCode != null){
//            val snippetCodeFlow = stringToFlow(snippetCode)
//            val runner: PrintscriptRunner = CommonPrintScriptRunner()
//            val isValidCode = runner.runAnalyzing(snippetCodeFlow, configFile)
//            return ResponseEntity(, HttpStatus.OK)
//        }
//        return ResponseEntity(HttpStatus.BAD_REQUEST)
//    }
    fun stringToFlow(text: String): Flow<String> {
        return flow {
            emit(text)
        }
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

}