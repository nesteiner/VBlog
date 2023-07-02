package com.example.backend.controller

import com.example.backend.exception.LoginException
import com.example.backend.exception.NoSuchUserException
import com.example.backend.exception.UserNotEnabledException
import com.example.backend.request.LoginRequest
import com.example.backend.response.LoginResponse
import com.example.backend.service.UserService
import com.example.backend.utils.JwtTokenUtil
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
@Validated
class AuthenticationController {
    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil
    @Autowired
    lateinit var userService: UserService

    @PostMapping("/authenticate")
    @Throws(LoginException::class, UserNotEnabledException::class, UsernameNotFoundException::class)
    fun createToken(@RequestBody @Valid request: LoginRequest, result: BindingResult): ResponseEntity<LoginResponse> {
        val userDetails = userService.loadUserByUsername(request.username)
        authenticate(request.username, request.passwordHash, userDetails)

        val token = jwtTokenUtil.generateToken(userDetails)
        return ResponseEntity.ok(LoginResponse(token))
    }

    fun authenticate(username: String, password: String, userDetails: UserDetails) {
        try {
            val user = userService.findOne(username) ?: throw NoSuchUserException("no such user ${username}")

            if (!user.enabled) {
                throw UserNotEnabledException("user ${username} not enabled")
            }

            if (password != userDetails.password) {
                throw LoginException("password error")
            } else {
                val authentication: Authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (exception: DisabledException) {
            throw LoginException("user diabled")
        } catch (exception: BadCredentialsException) { // this is for catching UsernameNotfoundException
            throw LoginException("in AuthenticationController: no such user or password error")
        }
    }
}