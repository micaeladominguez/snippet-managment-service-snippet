package com.example.snippetmanagmentservice.userRule

import com.example.snippetmanagmentservice.rule.Rule
import com.example.snippetmanagmentservice.rule.TypeOfRule
import com.example.snippetmanagmentservice.userRule.dto.RulesValues
import com.example.snippetmanagmentservice.userRule.dto.UpdateRules
import com.example.snippetmanagmentservice.userRule.dto.UserRuleGet
import com.example.snippetmanagmentservice.userRule.util.RuleFactory
import com.example.snippetmanagmentservice.userRule.util.ruleDefaultValues
import configuration.ConfigClasses
import configurationLinter.ConfigClassesLinter
import org.springframework.stereotype.Service
import java.util.UUID


@Service
class UserRuleService(private val userRuleRepository: UserRuleRepository) {

    fun getRulesForUser(userId: String, rules: List<Rule>) : UserRuleGet{
        if(userRuleRepository.hasAllRules(userId)){
           return getRulesForUserResponse(userId)
        }else{
            defineRulesForUser(userId, rules)
        }
       return getRulesForUserResponse(userId)
    }

    private fun defineRulesForUser(userId: String, rules: List<Rule>){
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


    fun getLintedRules(userId: String, rules: List<Rule>) : UserRuleGet {
        var rulesForUser = userRuleRepository.findByUserIdAndRuleType(userId, TypeOfRule.LINTER)
        if(rulesForUser.isEmpty()){
            defineRulesForUser(userId, rules)
            rulesForUser = userRuleRepository.findByUserIdAndRuleType(userId, TypeOfRule.FORMATTER)
        }
        return formatRules(rulesForUser, userId)
    }

    fun updateFormattingRules(userId: String, rules: UpdateRules) : List<UserRule> {
        updateRules(userId, rules)
        return userRuleRepository.findByUserIdAndRuleType(userId, TypeOfRule.FORMATTER)
    }

    private fun updateRules(userId: String, rules: UpdateRules) {
        for(rule in rules.rules){
            val createdRule = userRuleRepository.findByUserIdAndRuleId(userId, rule.ruleId)
            if(createdRule != null){
                userRuleRepository.save(UserRule(createdRule.id, userId, createdRule.rule, rule.value))
            }
        }
    }
    fun updateLintingRules(userId: String, rules: UpdateRules) : List<UserRule> {
        updateRules(userId, rules)
        return userRuleRepository.findByUserIdAndRuleType(userId, TypeOfRule.LINTER)
    }

    fun getFormattedRules(userId: String, rules: List<Rule>) : UserRuleGet {
        var rulesForUser = userRuleRepository.findByUserIdAndRuleType(userId, TypeOfRule.FORMATTER)
        if(rulesForUser.isEmpty()){
            defineRulesForUser(userId, rules)
            rulesForUser = userRuleRepository.findByUserIdAndRuleType(userId, TypeOfRule.FORMATTER)
        }
        return formatRules(rulesForUser, userId)
    }


    fun getFormattedRulesList(userId: String, rules: List<Rule>) :  ArrayList<ConfigClasses>{
        val list = ArrayList<ConfigClasses>()
        var rulesForUser = userRuleRepository.findByUserIdAndRuleType(userId, TypeOfRule.FORMATTER)
        if(rulesForUser.isEmpty()){
            defineRulesForUser(userId, rules)
            rulesForUser = userRuleRepository.findByUserIdAndRuleType(userId, TypeOfRule.FORMATTER)
        }
        for(formattedRule in rulesForUser){
            val configClass = RuleFactory.createRuleForFormatter(formattedRule.rule.name, formattedRule.value)
            if(configClass != null){
                list.add(configClass)
            }
        }
        return list
    }

    fun getLintedRulesList(userId: String, rules: List<Rule>) : ArrayList<ConfigClassesLinter>{
        val list = ArrayList<ConfigClassesLinter>()
        var rulesForUser = userRuleRepository.findByUserIdAndRuleType(userId, TypeOfRule.LINTER)
        if(rulesForUser.isEmpty()){
            defineRulesForUser(userId, rules)
            rulesForUser = userRuleRepository.findByUserIdAndRuleType(userId, TypeOfRule.LINTER)
        }
        for(lintedRules in rulesForUser){
            val configClass = RuleFactory.createRuleForLinter(lintedRules.rule.name, lintedRules.value)
            if(configClass.isNotEmpty()){
                list.addAll(configClass)
            }
        }
        return list
    }
}