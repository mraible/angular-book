package com.okta.developer.notes

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.jwt.*
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfiguration {

    @Bean
    fun webSecurity(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authz ->
                authz.anyRequest().authenticated()
            }
            .oauth2Login(withDefaults())
            .oauth2ResourceServer().jwt()

        http.cors()

        http.requiresChannel().requestMatchers(RequestMatcher { r ->
            r.getHeader("X-Forwarded-Proto") != null
        }).requiresSecure()

        http.headers()
            .contentSecurityPolicy("script-src 'self'; report-to /csp-report-endpoint/")

        return http.build()
    }

    @Value("#{ @environment['allowed.origins'] ?: {} }")
    private lateinit var allowedOrigins: List<String>

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.allowedOrigins = allowedOrigins
        config.allowedMethods = listOf("*")
        config.allowedHeaders = listOf("*")
        source.registerCorsConfiguration("/**", config)
        return source
    }
}
