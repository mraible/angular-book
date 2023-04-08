package com.okta.developer.notes

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class UserController(val repository: NotesRepository) {

    @GetMapping("/user/notes")
    fun notes(principal: Principal, title: String?, pageable: Pageable): Page<Note> {
        val jwt: JwtAuthenticationToken = principal as JwtAuthenticationToken
        val email = jwt.tokenAttributes.get("https://angular-book.com/email").toString()
        println("Fetching notes for user: ${email}")
        return if (title.isNullOrEmpty()) {
            repository.findAllByUsername(email, pageable)
        } else {
            println("Searching for title: ${title}")
            repository.findAllByUsernameAndTitleContainingIgnoreCase(email, title, pageable)
        }
    }

    @GetMapping("/user")
    fun user(@AuthenticationPrincipal user: OidcUser): OidcUser {
        return user
    }
}
