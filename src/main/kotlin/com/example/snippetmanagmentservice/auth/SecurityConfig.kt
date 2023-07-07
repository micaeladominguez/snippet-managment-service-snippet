package com.example.snippetmanagmentservice.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.*
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
                it/*.anyRequest().permitAll()*/
                .requestMatchers(GET, "/rules").authenticated()
                .requestMatchers(GET, "/snippets/").authenticated()
                .requestMatchers(GET, "/snippets").authenticated()
                .requestMatchers(POST, "/snippets/create").authenticated()
                .requestMatchers(PUT, "/snippets/update/snippet").authenticated()
                .requestMatchers(PUT, "/snippets/format/snippet").authenticated()
                .requestMatchers(PUT, "/snippets/format/code").authenticated()
                .requestMatchers(PUT, "/snippets/validate").authenticated()
                .requestMatchers(PUT, "/snippets/run").authenticated()
                .requestMatchers(DELETE, "/snippets").authenticated()
                .requestMatchers(GET, "/user/rules").authenticated()
                .requestMatchers(GET, "/user/rules/formatted").authenticated()
                .requestMatchers(GET, "/user/rules/linted").authenticated()
                .requestMatchers(PUT, "/user/rules/formatted").authenticated()
                .requestMatchers(PUT, "/user/rules/linted").authenticated()
                .requestMatchers(POST, "/user/rules").authenticated()
                .requestMatchers(PUT, "/user/rules").authenticated()
                .requestMatchers(POST, "/linting").authenticated()
                .anyRequest().denyAll()
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