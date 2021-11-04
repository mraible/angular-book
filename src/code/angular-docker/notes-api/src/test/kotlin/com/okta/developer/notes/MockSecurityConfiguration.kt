package com.okta.developer.notes

import org.mockito.Mockito.mock
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.jwt.JwtDecoder

@TestConfiguration
class MockSecurityConfiguration {
    private val clientRegistration: ClientRegistration

    @Bean
    fun clientRegistrationRepository(): ClientRegistrationRepository {
        return InMemoryClientRegistrationRepository(clientRegistration)
    }

    private fun clientRegistration(): ClientRegistration.Builder {
        val metadata: MutableMap<String, Any> = HashMap()
        metadata["end_session_endpoint"] = "https://angular.org/logout"
        return ClientRegistration.withRegistrationId("okta")
            .redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .scope("read:user")
            .authorizationUri("https://angular.org/login/oauth/authorize")
            .tokenUri("https://angular.org/login/oauth/access_token")
            .jwkSetUri("https://angular.org/oauth/jwk")
            .userInfoUri("https://api.angular.org/user")
            .providerConfigurationMetadata(metadata)
            .userNameAttributeName("id")
            .clientName("Client Name")
            .clientId("client-id")
            .clientSecret("client-secret")
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return mock(JwtDecoder::class.java)
    }

    @Bean
    fun authorizedClientService(clientRegistrationRepository: ClientRegistrationRepository?): OAuth2AuthorizedClientService {
        return InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository)
    }

    @Bean
    fun authorizedClientRepository(authorizedClientService: OAuth2AuthorizedClientService?): OAuth2AuthorizedClientRepository {
        return AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService)
    }

    init {
        clientRegistration = clientRegistration().build()
    }
}
