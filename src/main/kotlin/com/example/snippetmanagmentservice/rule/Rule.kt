package com.example.snippetmanagmentservice.rule

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.*


@Entity
data class Rule(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    val name: String,

    val typeOfRule: TypeOfRule
){
    constructor(): this(UUID.randomUUID(), "", TypeOfRule.FORMATTER)
}