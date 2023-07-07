package com.example.snippetmanagmentservice.userRule

import com.example.snippetmanagmentservice.auth.IdExtractor
import com.example.snippetmanagmentservice.redis.route.StreamTestRoute
import com.example.snippetmanagmentservice.rule.RuleService
import com.example.snippetmanagmentservice.snippet.SnippetService
import com.example.snippetmanagmentservice.userRule.dto.UpdateRules
import com.example.snippetmanagmentservice.userRule.dto.UpdateRulesAndSnippets
import com.example.snippetmanagmentservice.userRule.dto.UserRuleGet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("/user/rules")
class UserRuleController(private val userRuleService: UserRuleService, private val ruleService: RuleService, private val snippetService: SnippetService, private val streamRoute : StreamTestRoute) {

    @GetMapping()
    fun getRules(@RequestParam("userId") userId: String): ResponseEntity<UserRuleGet> {
        val rules = ruleService.getRules()
        return ResponseEntity(userRuleService.getRulesForUser(userId, rules), HttpStatus.OK)
    }

    @GetMapping("/formatted")
    fun getRulesFormatted(authentication: Authentication): ResponseEntity<UserRuleGet> {
        val userID = IdExtractor.getId(authentication)
        val rules = ruleService.getRules()
        return ResponseEntity(userRuleService.getFormattedRules(userID, rules), HttpStatus.OK)
    }

    @GetMapping("/linted")
    fun getRulesLinted(authentication: Authentication): ResponseEntity<UserRuleGet> {
        val userID = IdExtractor.getId(authentication)
        val rules = ruleService.getRules()
        return ResponseEntity(userRuleService.getLintedRules(userID, rules), HttpStatus.OK)
    }


    @PutMapping("/formatted")
    fun updateFormattingRules(authentication: Authentication, @RequestBody rules: UpdateRules): ResponseEntity<List<UserRule>>{
        val userId =  IdExtractor.getId(authentication)
        return ResponseEntity(userRuleService.updateFormattingRules(userId, rules), HttpStatus.OK)
    }

    @PutMapping("/linted")
    suspend fun updateLintingRules(authentication: Authentication, @RequestBody updateRules: UpdateRulesAndSnippets): ResponseEntity<List<UserRule>>{
        val userId =  IdExtractor.getId(authentication)
        val userID = IdExtractor.getId(authentication)
        val rulesUpdated = userRuleService.updateLintingRules(userId, updateRules.rules)
        val rules = ruleService.getRules()
        val lintingRules = userRuleService.getLintedRulesList(userID, rules)
        val snippets = snippetService.findSnippets(updateRules.snippetsUuid)
        GlobalScope.async {streamRoute.updateLinting(snippets, lintingRules)}
        return ResponseEntity(rulesUpdated, HttpStatus.OK)
    }
}