package com.example.snippetmanagmentservice.snippet

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/snippets")
class SnippetController(private val snippetService: SnippetService) {

    @PostMapping
    fun createSnippet(@RequestBody snippet: Snippet): ResponseEntity<Snippet> {
        val createdSnippet = snippetService.saveSnippet(snippet)
        return ResponseEntity(createdSnippet, HttpStatus.CREATED)
    }

    @PutMapping("/{uuid}")
    fun updateSnippet(@PathVariable uuid: UUID, @RequestBody newCode: String): ResponseEntity<Snippet> {
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
}