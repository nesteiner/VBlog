package com.example.backend.controller

import com.example.backend.exception.RegisterException
import com.example.backend.model.User
import com.example.backend.utils.Response
import com.example.backend.request.RegisterRequest
import com.example.backend.service.UserService
import com.example.backend.utils.JwtTokenUtil
import com.example.backend.utils.Status
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
@RequestMapping("/user")
class UserController {
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil

    @PostMapping("/register")
    @Throws(RegisterException::class)
    fun insertOne(@RequestBody @Valid request: RegisterRequest, result: BindingResult): Response<User> {
        return Response.Ok("register ok", userService.insertOne(request))
    }

    @DeleteMapping
    fun deleteOne(servletRequest: HttpServletRequest): Response<Status> {
        val username = jwtTokenUtil.getUsernameFromRequest(servletRequest)
        val user = userService.findOne(username)
        user?.let {
            userService.deleteOne(it.id!!)
        }

        return Response.Ok("delete ok", Status.Ok)
    }

    @GetMapping
    fun findOne(servletRequest: HttpServletRequest): Response<User> {
        val username = jwtTokenUtil.getUsernameFromRequest(servletRequest)
        val user = userService.findOne(username)
        return user?.let {
            Response.Ok("this user", it)
        } ?: Response.Err("no such user")
    }
}