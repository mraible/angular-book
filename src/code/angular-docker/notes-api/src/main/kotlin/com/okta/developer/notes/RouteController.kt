package com.okta.developer.notes

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class RouteController {

    @RequestMapping(value = ["/{path:[^\\.]*}"])
    fun redirect(request: HttpServletRequest): String {
        return "forward:/"
    }
}
