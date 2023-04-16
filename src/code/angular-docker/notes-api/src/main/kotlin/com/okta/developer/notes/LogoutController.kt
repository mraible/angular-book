package com.okta.developer.notes

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LogoutController(clientRegistrationRepository: ClientRegistrationRepository) {

    val registration: ClientRegistration = clientRegistrationRepository.findByRegistrationId("okta")

    @PostMapping("/api/logout")
    fun logout(request: HttpServletRequest,
               @AuthenticationPrincipal(expression = "idToken") idToken: OidcIdToken): ResponseEntity<*> {
        val issuerUri = registration.providerDetails.issuerUri
        val originUrl = request.getHeader(HttpHeaders.ORIGIN)
        val logoutUrl = "${issuerUri}v2/logout?client_id=${registration.clientId}&returnTo=${originUrl}"
        request.session.invalidate()
        return ResponseEntity.ok().body(java.util.Map.of("logoutUrl", logoutUrl))
    }
}
