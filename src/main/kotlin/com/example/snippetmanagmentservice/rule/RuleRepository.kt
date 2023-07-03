package com.example.snippetmanagmentservice.rule

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RuleRepository : JpaRepository<Rule, UUID>