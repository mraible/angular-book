package com.okta.developer.notes

import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry

@Configuration
class RestConfiguration : RepositoryRestConfigurer {

    override fun configureRepositoryRestConfiguration(
        config: RepositoryRestConfiguration?,
        cors: CorsRegistry?
    ) {
        config?.exposeIdsFor(Note::class.java)
        config?.setBasePath("/api")
    }
}
