package com.example.snippetmanagmentservice.snippet

import org.springframework.stereotype.Service
import java.util.*

@Service
class SnippetService(private val snippetRepository: SnippetRepository) {

    fun saveSnippet(snippet: Snippet): Snippet {
        return snippetRepository.save(snippet)
    }

    fun updateSnippet(uuid: UUID, newCode: String): Snippet {
        val snippet = snippetRepository.findSnippetByUuid(uuid) ?: throw RuntimeException("Snippet not found")
        val updatedSnippet = snippet.copy(code = newCode)
        return snippetRepository.save(updatedSnippet)
    }

    fun findSnippet(uuid: UUID): Snippet {
        return snippetRepository.findSnippetByUuid(uuid) ?: throw RuntimeException("Snippet not found")
    }

    fun findSnippets(uuids: List<UUID>): List<Snippet> {
        return snippetRepository.findAllById(uuids)
    }

    fun deleteSnippet(uuid: UUID) {
        val snippet = snippetRepository.findSnippetByUuid(uuid) ?: throw RuntimeException("Snippet not found")
        snippetRepository.delete(snippet)
    }
}