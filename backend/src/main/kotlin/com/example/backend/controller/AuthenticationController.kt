package com.example.backend.controller

import com.example.backend.exception.LoginException
import com.example.backend.request.LoginRequest
import com.example.backend.response.LoginResponse
import com.example.backend.utils.JwtTokenUtil
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
@Validated
class AuthenticationController {
    @Autowired
    lateinit var authenticationManager: AuthenticationManager;

    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil;

    @Autowired
    lateinit var userDetailsService: UserDetailsService;

    @PostMapping("/authenticate")
    @Throws(LoginException::class)
    fun createToken(@RequestBody @Valid request: LoginRequest, result: BindingResult): ResponseEntity<LoginResponse> {
        authenticate(request.username, request.password)
        val userDetails = userDetailsService.loadUserByUsername(request.username)
        val token = jwtTokenUtil.generateToken(userDetails)
        return ResponseEntity.ok(LoginResponse(token))
    }

    @Throws(LoginException::class)
    fun authenticate(username: String, password: String) {
        try {
            val user = userDetailsService.loadUserByUsername(username)
            if (password != user.password) {
                throw LoginException("password error")
            } else {
                val authentication: Authentication = UsernamePasswordAuthenticationToken(user, null, user.authorities)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (exception: DisabledException) {
            throw LoginException("user diabled")
        } catch (exception: BadCredentialsException) { // this is for catching UsernameNotfoundException
            throw LoginException("in AuthenticationController: no such user or password error")
        }
    }
}