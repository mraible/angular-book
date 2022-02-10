package com.okta.developer.notes

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter
import org.springframework.security.web.util.matcher.RequestMatcher

@Configuration
class SecurityConfiguration {

    @Bean
    fun webSecurity(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorize ->
                authorize.antMatchers("/**/*.{js,html,css}").permitAll()
                authorize.antMatchers("/", "/user").permitAll()
                authorize.anyRequest().authenticated()
            }
            .oauth2Login()
            .and()
            .oauth2ResourceServer().jwt()

        http.cors()

        http.requiresChannel().requestMatchers(RequestMatcher { r ->
            r.getHeader("X-Forwarded-Proto") != null
        }).requiresSecure()

        http.csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

        http.headers()
            .contentSecurityPolicy("script-src 'self' 'unsafe-inline'; report-to /csp-report-endpoint/")
            .and()
            .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN)
            .and()
            .permissionsPolicy().policy("geolocation=(self), microphone=(), accelerometer=(), camera=()")

        return http.build();
    }
}
