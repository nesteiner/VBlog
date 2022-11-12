package com.example.backend.controller

import com.example.backend.exception.NoSuchUserException
import com.example.backend.model.User
import com.example.backend.request.UpdateUserNameRequest
import com.example.backend.request.UpdateUserRolesRequest
import com.example.backend.service.UserService
import com.example.backend.utils.Response
import com.example.backend.utils.Status
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
@Validated
class AdminController {
    @Autowired
    lateinit var userService: UserService

    @GetMapping("/user")
    fun findAll(): Response<List<User>> {
        return Response.Ok("all user", userService.findAll())
    }

    @PutMapping("/user/role")
    @Throws(NoSuchUserException::class)
    fun updateRoles(@RequestBody @Valid request: UpdateUserRolesRequest, result: BindingResult): Response<User> {
        val userid = request.id
        val user = userService.findOne(userid)
        user?.let {
            it.roles = request.roles
            userService.updateOne(it)
            return Response.Ok("update ok", it)
        } ?: throw NoSuchUserException("no such user")
    }

    @PutMapping("/user/name")
    @Throws(NoSuchUserException::class)
    fun updateName(@RequestBody @Valid request: UpdateUserNameRequest, result: BindingResult): Response<User> {
        val userid = request.id!!
        val user = userService.findOne(userid)
        user?.let {
            it.name = request.name
            userService.updateOne(it) // ATTENTION: this may throw an error
            return Response.Ok("update ok", it)
        } ?: throw NoSuchUserException("no such user")
    }

    @DeleteMapping("/user/{id}")
    @Throws(NoSuchUserException::class)
    fun deleteUser(@PathVariable id: Long): Response<Status> {
        userService.deleteOne(id)
        return Response.Ok("delete ok", Status.Ok)
    }
}