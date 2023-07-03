package com.example.snippetmanagmentservice.userRule

import com.example.snippetmanagmentservice.rule.RuleService
import com.example.snippetmanagmentservice.userRule.dto.UserRuleGet
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("/user/rules")
class UserRuleController(private val userRuleService: UserRuleService, private val ruleService: RuleService) {

    @GetMapping()
    fun getRules(@RequestParam("userId") userId: String): ResponseEntity<UserRuleGet> {
        val rules = ruleService.getRules()
        return ResponseEntity(userRuleService.getRulesForUser(userId, rules), HttpStatus.OK)
    }

    @GetMapping("/formatted")
    fun getRulesFormatted(@RequestParam("userId") userId: String): ResponseEntity<UserRuleGet> {
        return ResponseEntity(userRuleService.getFormattedRules(userId), HttpStatus.OK)
    }

    @GetMapping("/linted")
    fun getRulesLinted(@RequestParam("userId") userId: String): ResponseEntity<UserRuleGet> {
        return ResponseEntity(userRuleService.getLintedRules(userId), HttpStatus.OK)
    }
}