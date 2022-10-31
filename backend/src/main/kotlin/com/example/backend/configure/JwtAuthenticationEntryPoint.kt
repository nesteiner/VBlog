package com.example.backend.configure

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationEntryPoint: AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json; charset=utf-8"
        val mapper = ObjectMapper()
        val node: ObjectNode = mapper.createObjectNode()
        node.put("status", "access failed")
        node.put("message", "in JwtAuthenticationEntryPoint: " + authException.message)
        val json: String = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node)
        response.writer.write(json)
    }
}