package com.okta.developer.notes

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

/**
 * Spring Security 6 doesn't set a XSRF-TOKEN cookie by default.
 * This solution is
 * [
 * recommended by Spring Security.](https://github.com/spring-projects/spring-security/issues/12141#issuecomment-1321345077)
 */
class CookieCsrfFilter : OncePerRequestFilter() {
    /** {@inheritDoc}  */
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val csrfToken = request.getAttribute(CsrfToken::class.java.name) as CsrfToken
        response.setHeader(csrfToken.headerName, csrfToken.token)
        filterChain.doFilter(request, response)
    }
}
