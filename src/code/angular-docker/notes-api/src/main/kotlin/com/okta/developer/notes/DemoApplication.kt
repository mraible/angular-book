package com.okta.developer.notes

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@Entity
data class Note(
    @Id @GeneratedValue var id: Long? = null,
    var title: String? = null,
    var text: String? = null,
    @JsonIgnore var username: String? = null
)

@RepositoryRestResource
interface NotesRepository : JpaRepository<Note, Long> {
    fun findAllByUsername(name: String, pageable: Pageable): Page<Note>
    fun findAllByUsernameAndTitleContainingIgnoreCase(name: String, term: String, pageable: Pageable): Page<Note>
}

@Component
@RepositoryEventHandler(Note::class)
class AddUserToNote {

    @HandleBeforeCreate
    fun handleCreate(note: Note) {
        val email = SecurityContextHolder.getContext().authentication.name
        note.username = email
        println("Creating note: $note")
    }
}
