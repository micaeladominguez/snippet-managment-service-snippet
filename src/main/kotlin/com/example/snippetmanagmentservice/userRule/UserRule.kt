package com.example.snippetmanagmentservice.userRule

import com.example.snippetmanagmentservice.rule.Rule
import jakarta.persistence.*
import java.util.*

@Entity
data class UserRule(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    val userId: String,

    @ManyToOne
    val rule: Rule,

    val value: String
){
    constructor() : this(UUID.randomUUID(), "", Rule(), "")
}