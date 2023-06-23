package com.example.snippetmanagmentservice.snippet

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SnippetRepository: JpaRepository<Snippet, UUID> {
    fun findSnippetByUuid(uuid: UUID): Snippet?
}