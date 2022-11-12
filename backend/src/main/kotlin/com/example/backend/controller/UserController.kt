package com.example.backend.controller

import com.example.backend.exception.NoSuchUserException
import com.example.backend.exception.RegisterException
import com.example.backend.model.User
import com.example.backend.utils.Response
import com.example.backend.request.RegisterRequest
import com.example.backend.request.UpdatePasswordRequest
import com.example.backend.request.UpdateRolesRequest
import com.example.backend.request.UpdateUserNameRequest
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
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
    @Throws(NoSuchUserException::class)
    fun deleteOne(servletRequest: HttpServletRequest): Response<Status> {
        val username = jwtTokenUtil.getUsernameFromRequest(servletRequest)
        val user = userService.findOne(username)
        if(user == null) {
            throw NoSuchUserException("no such user ${username}")
        } else {
            userService.deleteOne(user.id!!)
            return Response.Ok("delete ok", Status.Ok)
        }

    }

    @GetMapping
    @Throws(NoSuchUserException::class)
    fun findOne(servletRequest: HttpServletRequest): Response<User> {
        val username = jwtTokenUtil.getUsernameFromRequest(servletRequest)
        val user = userService.findOne(username)
        if(user == null) {
            throw NoSuchUserException("no such user ${username}")
        } else {
            return Response.Ok("this user", user)
        }
    }

    @PutMapping("/name")
    @Throws(NoSuchUserException::class)
    fun updateName(servletRequest: HttpServletRequest, @RequestBody @Valid request: UpdateUserNameRequest, result: BindingResult): Response<User> {
        val username = jwtTokenUtil.getUsernameFromRequest(servletRequest)
        val user = userService.findOne(username)
        if(user == null) {
            throw NoSuchUserException("no such user ${username}")
        } else {
            user.name = request.name
            val newuser = userService.updateOne(user)
            return Response.Ok("update ok", newuser)
        }
    }

    @PutMapping("/password")
    @Throws(NoSuchUserException::class)
    fun updatePassword(servletRequest: HttpServletRequest, @RequestBody @Valid request: UpdatePasswordRequest, result: BindingResult): Response<User> {
        val username = jwtTokenUtil.getUsernameFromRequest(servletRequest)
        val user = userService.findOne(username)
        if(user == null) {
            throw NoSuchUserException("no such user ${username}")
        } else {
            user.passwordHash = request.passwordHash
            val newuser = userService.updateOne(user)
            return Response.Ok("update ok", newuser)
        }
    }
}