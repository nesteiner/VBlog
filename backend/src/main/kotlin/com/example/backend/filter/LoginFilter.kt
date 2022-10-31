package com.example.backend.filter

import com.example.backend.utils.JwtTokenUtil
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
@Slf4j
class LoginFilter : OncePerRequestFilter() {
    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        response.setContentType("application/json; charset=utf-8")
        val requestTokenHeader: String? = request.getHeader("Authorization")
        var username: String? = null
        var jwtToken: String? = null
        val mapper = ObjectMapper()
        // enter request logic
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7)
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken)
                request.setAttribute("username", username)
                request.setAttribute("jwtToken", jwtToken)
            } catch (exception: IllegalArgumentException) {
                logger.error("unable to get jwt token")
                val node: ObjectNode = mapper.createObjectNode()
                node.put("status", "access failed")
                node.put("message", "in LoginFilter: unable to get jwt token")
                val json: String = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node)
                response.setStatus(401)
                response.getWriter().write(json)
                return
            } catch (exception: ExpiredJwtException) {
                logger.error("jwt token has been expired")
                val node: ObjectNode = mapper.createObjectNode()
                node.put("status", "access failed")
                node.put("message", "in LoginFilter: jwt token has been expired")
                val json: String = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node)
                response.setStatus(401)
                response.getWriter().write(json)
                return
            } catch (exception: Exception) {
                logger.error("unknown error")
                val node: ObjectNode = mapper.createObjectNode()
                node.put("status", "access failed")
                node.put("message", "in LoginFilter: jwt token parse error")
                val json: String = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node)
                response.setStatus(400)
                response.getWriter().write(json)
                return
            } finally {
//                response.setHeader("Access-Control-ALlow-Origin", "*");
            }
        } else if (requestTokenHeader != null) {
            logger.warn("jwt token does not begin with bearer string")
            val node: ObjectNode = mapper.createObjectNode()
            node.put("status", "access failed")
            node.put("message", "in LoginFilter: jwt token parse error")
            val json: String = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node)
            response.setStatus(400)
            //            response.setHeader("Access-Control-ALlow-Origin", "*");
            response.getWriter().write(json)
            return
        }
        filterChain.doFilter(request, response)
    }
}