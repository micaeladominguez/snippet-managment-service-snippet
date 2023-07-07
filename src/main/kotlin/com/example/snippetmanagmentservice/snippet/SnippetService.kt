package com.example.snippetmanagmentservice.snippet

import com.example.snippetmanagmentservice.snippet.dto.SnippetPostDTO
import org.springframework.stereotype.Service
import java.util.*

@Service
class SnippetService(private val snippetRepository: SnippetRepository) {

    fun allSnippets(): List<Snippet> {
        return snippetRepository.findAll().toList()
    }
    fun saveSnippet(snippet: SnippetPostDTO, linesFailed: String): Snippet {
        return snippetRepository.save(Snippet(UUID.randomUUID(), snippet.name, snippet.type, snippet.code, linesFailed))
    }

    fun updateSnippet(uuid: UUID, newCode: String, newlinesFailed: String): Snippet {
        val snippet = snippetRepository.findSnippetById(uuid) ?: throw RuntimeException("Snippet not found")
        val updatedSnippet = snippet.copy(code = newCode, linesFailed = newlinesFailed)
        return snippetRepository.save(updatedSnippet)
    }

    fun findSnippet(uuid: UUID): Snippet {
        return snippetRepository.findSnippetById(uuid) ?: throw RuntimeException("Snippet not found")
    }

    fun findSnippets(uuids: List<UUID>): List<Snippet> {
        return snippetRepository.findAllById(uuids)
    }

    fun deleteSnippet(uuid: UUID) {
        val snippet = snippetRepository.findSnippetById(uuid) ?: throw RuntimeException("Snippet not found")
        snippetRepository.delete(snippet)
    }
}