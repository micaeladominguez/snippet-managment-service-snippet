package com.example.snippetmanagmentservice.redis.rule

import configurationLinter.ConfigClassesLinter

class LintedRules {
    companion object {
        fun getLintedRules(rules: ArrayList<Rule>) : ArrayList<ConfigClassesLinter> {
            val lintedRules = ArrayList<ConfigClassesLinter>()
            for(rule in rules){
                val configClass = RuleFactory.createRuleForLinter(rule.name, rule.value)
                if(configClass != null){
                    lintedRules.add(configClass)
                }
            }
            return lintedRules
        }
    }
}