package com.okta.developer.notes

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class UserController(val repository: NotesRepository) {

    @GetMapping("/user/notes")
    fun notes(principal: Principal): List<Note> {
        println("Fetching notes for user: ${principal.name}")
        return repository.findAllByUser(principal.name)
    }

    @GetMapping("/user")
    fun user(@AuthenticationPrincipal user: OidcUser): OidcUser {
        return user;
    }
}
