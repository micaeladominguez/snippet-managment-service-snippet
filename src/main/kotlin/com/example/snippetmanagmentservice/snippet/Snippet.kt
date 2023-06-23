package com.example.snippetmanagmentservice.snippet

import jakarta.persistence.*
import java.util.UUID

@Entity
data class Snippet(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @Column(name = "name")
    val name: String,

    @Column(name = "type")
    val type: String,

    @Column(name = "code")
    val code: String
)