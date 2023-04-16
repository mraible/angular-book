package com.okta.developer.notes

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class SpaWebFilter : OncePerRequestFilter() {

    /**
     * Forwards any unmapped paths (except those containing a period) to `index.html`.
     */
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.requestURI
        if (!path.startsWith("/api") &&
            !path.startsWith("/login") &&
            !path.startsWith("/oauth2") &&
            !path.startsWith("/user") &&
            !path.contains(".") &&
            path.matches("/(.*)".toRegex())
        ) {
            request.getRequestDispatcher("/index.html").forward(request, response)
            return
        }
        filterChain.doFilter(request, response)
    }
}
