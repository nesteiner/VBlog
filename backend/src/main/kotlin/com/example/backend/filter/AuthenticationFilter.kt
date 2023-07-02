package com.example.backend.filter

import com.example.backend.exception.UserNotEnabledException
import com.example.backend.model.User
import com.example.backend.service.UserService
import com.example.backend.utils.JwtTokenUtil
import com.example.backend.utils.Response
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthenticationFilter: OncePerRequestFilter() {
    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil
    @Autowired
    lateinit var userService: UserService

    @Throws(UsernameNotFoundException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val username = request.getAttribute("username") as String?
        val jwttoken = request.getAttribute("jwtToken") as String?
        val services = listOf(userService)

        if(username != null && jwttoken != null && SecurityContextHolder.getContext().authentication == null) {
            try {
                // val userdetails = studentService.loadUserByUsername(username)
                var userdetails: UserDetails? = null
                var user: User? = null
                for (service in services) {
                    user = service.findOne(username)
                    if (user != null) {
                        userdetails = service.loadUserByUsername(username)
                        break
                    }
                }

                if (userdetails == null) {
                    throw UsernameNotFoundException("username not found: ${username}")
                }

                if (user != null && !user.enabled) {
                    throw UserNotEnabledException("user ${username} not enabled")
                }

                if(jwtTokenUtil.validateToken(jwttoken, userdetails)) {
                    val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                        userdetails, null, userdetails.authorities
                    )

                    usernamePasswordAuthenticationToken.setDetails(WebAuthenticationDetailsSource().buildDetails(request))
                    SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
                }
            } catch (exception: UsernameNotFoundException) {
                val objectMapper = ObjectMapper()
                val result = Response.Err("username not found")
                response.status = 400
                response.writer.write(objectMapper.writeValueAsString(result))
                return
            } catch (exception: UserNotEnabledException) {
                val objectMapper = ObjectMapper()
                val result = Response.Err(exception.message)
                response.status = HttpStatus.BAD_REQUEST.value()
                response.writer.write(objectMapper.writeValueAsString(result))
                return
            }

        }

        filterChain.doFilter(request, response)
    }
}