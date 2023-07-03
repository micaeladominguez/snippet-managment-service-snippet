package com.example.snippetmanagmentservice.auth

import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.Jwt

class IdExtractor {
    companion object {
        fun getId(authentication: Authentication) : String {
            val jwtToken = authentication.principal as Jwt
            val id = extractUserId(jwtToken)
            println("ID : $id")
            return id
        }

        private fun extractUserId(jwtToken: Jwt): String {
            val claims: Map<String, Any> = jwtToken.claims
            return claims["sid"].toString()
        }
    }
}