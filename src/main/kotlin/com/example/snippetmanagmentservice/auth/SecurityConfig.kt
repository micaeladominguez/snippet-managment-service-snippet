package com.example.snippetmanagmentservice.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.jwt.*
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Value("\${auth0.audience}")
    val audience: String,
    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    val issuer: String,
) {

    @Bean
    fun securityWebFilterChain(http: HttpSecurity): DefaultSecurityFilterChain {
        return http
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
                /*.requestMatchers(GET, "/test").hasAuthority("SCOPE_read:snippets")
                .requestMatchers(GET, "/authorization").hasAuthority("SCOPE_read:snippets")
                .requestMatchers(POST, "/authorization").hasAuthority("SCOPE_read:snippets")
                .requestMatchers(PUT, "/authorization").hasAuthority("SCOPE_read:snippets")
                .anyRequest().denyAll()*/
            }
            .oauth2ResourceServer { it.jwt { jwt -> jwt.decoder(jwtDecoder()) } }
            .cors(withDefaults())
            .csrf { csrf -> csrf.disable() }
            .build()
    }


    @Bean
    fun jwtDecoder(): JwtDecoder {
        val audienceValidator: OAuth2TokenValidator<Jwt> = AudienceValidator(audience)
        val withIssuer: OAuth2TokenValidator<Jwt> = JwtValidators.createDefaultWithIssuer(issuer)
        val withAudience: OAuth2TokenValidator<Jwt> = DelegatingOAuth2TokenValidator(withIssuer, audienceValidator)

        val jwtDecoder = JwtDecoders.fromOidcIssuerLocation<JwtDecoder>(issuer)
        (jwtDecoder as NimbusJwtDecoder).setJwtValidator(withAudience)

        return jwtDecoder
    }


}