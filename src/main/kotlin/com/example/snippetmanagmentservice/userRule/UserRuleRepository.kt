package com.example.snippetmanagmentservice.userRule

import com.example.snippetmanagmentservice.rule.TypeOfRule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface UserRuleRepository : JpaRepository<UserRule, UUID>{
    @Query("SELECT COUNT(DISTINCT ur.rule) = (SELECT COUNT(r) FROM Rule r) FROM UserRule ur WHERE ur.userId = :userId")
    fun hasAllRules(userId: String): Boolean
    fun findByUserId(userId: String): List<UserRule>

    fun findByUserIdAndRuleId(userId: String, ruleId: UUID): UserRule?


    @Query("SELECT ur FROM UserRule ur WHERE ur.userId = :userId and ur.rule.typeOfRule = :rule")
    fun findByUserIdAndRuleType(userId: String, rule: TypeOfRule) : List<UserRule>

}