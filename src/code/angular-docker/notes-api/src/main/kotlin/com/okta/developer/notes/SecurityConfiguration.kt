package com.okta.developer.notes

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter
import org.springframework.security.web.util.matcher.RequestMatcher

@Configuration
class SecurityConfiguration {

    @Bean
    fun webSecurity(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authz ->
                authz.requestMatchers("/", "/index.html", "/*.js", "/*.css", "/assets/**").permitAll()
                authz.requestMatchers("/user").permitAll()
                authz.anyRequest().authenticated()
            }
            .oauth2Login(withDefaults())
            .oauth2ResourceServer().jwt()

        http.cors()

        http.requiresChannel().requestMatchers(RequestMatcher { r ->
            r.getHeader("X-Forwarded-Proto") != null
        }).requiresSecure()

        http.csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(CsrfTokenRequestAttributeHandler())

        http.addFilterAfter(SpaWebFilter(), BasicAuthenticationFilter::class.java)
        http.addFilterAfter(CookieCsrfFilter(), BasicAuthenticationFilter::class.java)

        http.headers { headers ->
            headers.contentSecurityPolicy("script-src 'self' 'unsafe-inline'; report-to /csp-report-endpoint/")
            headers.frameOptions { frameOptions -> frameOptions.sameOrigin() }
            headers.referrerPolicy { referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN) }
            headers.permissionsPolicy { permissions ->
                permissions.policy(
                    "camera=(), fullscreen=(self), geolocation=(), gyroscope=(), " +
                        "magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()"
                )
            }
        }

        return http.build()
    }
}
