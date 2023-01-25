package com.example.backend.controller

import com.example.backend.exception.LoginException
import com.example.backend.request.LoginRequest
import com.example.backend.response.LoginResponse
import com.example.backend.service.AdminService
import com.example.backend.service.StudentService
import com.example.backend.utils.ErrorStatus
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
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@Validated
class AuthenticationController {
    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil
    @Autowired
    lateinit var studentService: StudentService
    @Autowired
    lateinit var adminService: AdminService

    @PostMapping("/authenticate", params = ["type"])
    @Throws(LoginException::class)
    fun createToken(@RequestBody @Valid request: LoginRequest, @RequestParam("type") type: String, result: BindingResult): ResponseEntity<LoginResponse> {
        var userDetailsService: UserDetailsService? = null

        if (type == "admin") {
            userDetailsService = adminService
        } else if(type == "student") {
            userDetailsService = studentService
        } else {
            throw LoginException("no such user type", ErrorStatus.NoSuchUser)
        }

        val userDetails = userDetailsService.loadUserByUsername(request.username)
        authenticate(request.username, request.passwordHash, userDetails)

        val token = jwtTokenUtil.generateToken(userDetails)
        return ResponseEntity.ok(LoginResponse(token))
    }

    @Throws(LoginException::class)
    fun authenticate(username: String, password: String, userDetails: UserDetails) {
        try {

            if (password != userDetails.password) {
                throw LoginException("password error", ErrorStatus.UserNamePasswordError)
            } else {
                val authentication: Authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (exception: DisabledException) {
            throw LoginException("user diabled", ErrorStatus.UserDisabled)
        } catch (exception: BadCredentialsException) { // this is for catching UsernameNotfoundException
            throw LoginException("in AuthenticationController: no such user or password error", ErrorStatus.UserNamePasswordError)
        }
    }
}