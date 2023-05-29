package com.example.snippetmanagmentservice.user

import jakarta.persistence.*
import java.util.*

@Entity
data class JJUser(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: UUID? = UUID.randomUUID(),
        @Column(name = "name")
        val name: String,
)

