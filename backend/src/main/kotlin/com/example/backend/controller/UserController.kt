package com.example.backend.controller

import com.example.backend.exception.NoSuchUserException
import com.example.backend.service.UserService
import com.example.backend.utils.Response
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {
    @Autowired
    lateinit var userService: UserService
    @GetMapping("/isadmin")
    fun isadmin(): Response<Boolean> {
        val user = SecurityContextHolder.getContext().authentication.principal as org.springframework.security.core.userdetails.User
        val ifuser = userService.findOne(user.username)
        if (ifuser == null) {
            throw NoSuchUserException("no such user ${user.username}")
        } else {
            println("hello ${ifuser.roles.map { it.name }}")
            return Response.Ok(
                "isadmin",
                ifuser.roles.map { it.name }
                    .any { it == "admin" }
            )
        }
    }

    @GetMapping("/name")
    fun currentUsername(): Response<String> {
        val user = SecurityContextHolder.getContext().authentication.principal as org.springframework.security.core.userdetails.User
        return Response.Ok("user name", user.username)
    }

}