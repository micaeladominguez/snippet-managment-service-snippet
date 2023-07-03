package com.example.snippetmanagmentservice.userRule

import com.example.snippetmanagmentservice.rule.Rule
import com.example.snippetmanagmentservice.rule.TypeOfRule
import com.example.snippetmanagmentservice.userRule.dto.RulesValues
import com.example.snippetmanagmentservice.userRule.dto.UserRuleGet
import com.example.snippetmanagmentservice.userRule.util.ruleDefaultValues
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserRuleService(private val userRuleRepository: UserRuleRepository) {

    fun getRulesForUser(userId: String, rules: List<Rule>) : UserRuleGet{
        if(userRuleRepository.hasAllRules(userId)){
           return getRulesForUserResponse(userId)
        }else{
            for(rule in rules){
                val defaultValue = ruleDefaultValues[rule.name]
                if(defaultValue != null){
                    val userRule = userRuleRepository.findByUserIdAndRuleId(userId, rule.id)
                    if(userRule == null){
                        userRuleRepository.save(UserRule(UUID.randomUUID(), userId, rule, defaultValue))
                    }
                }
            }
        }
       return getRulesForUserResponse(userId)
    }


    private fun getRulesForUserResponse(userId: String): UserRuleGet {
        val rulesForUser = userRuleRepository.findByUserId(userId)
        return formatRules(rulesForUser, userId)
    }


    private fun formatRules(userRules: List<UserRule>, userId: String): UserRuleGet {
        val ruleValues = mutableListOf<RulesValues>()
        for(userRule in userRules){
            ruleValues.add(RulesValues(userRule.rule.id,userRule.rule.name, userRule.rule.typeOfRule, userRule.value))
        }
        return UserRuleGet(userId, ruleValues)
    }


    fun getLintedRules(userId: String) : UserRuleGet {
        val rulesForUser = userRuleRepository.findByUserIdAndRuleType(userId, TypeOfRule.LINTER)
        return formatRules(rulesForUser, userId)
    }

    fun getFormattedRules(userId: String) : UserRuleGet {
        val rulesForUser = userRuleRepository.findByUserIdAndRuleType(userId, TypeOfRule.FORMATTER)
        return formatRules(rulesForUser, userId)
    }
}