package com.dopaminedefense.dodiserver.common.config

import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

@Component
class FaviconFilter : Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        if (!httpRequest.requestURI.endsWith("favicon.ico") &&
            !httpRequest.requestURI.endsWith("favicon-32x32.png")) {
            chain.doFilter(request, response)
        }
    }

    override fun init(filterConfig: FilterConfig?) {}
    override fun destroy() {}
}