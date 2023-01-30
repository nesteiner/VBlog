package com.example.backend.controller

import com.example.backend.exception.NoSuchUserException
import com.example.backend.model.Admin
import com.example.backend.model.User
import com.example.backend.request.RegisterAdminRequest
import com.example.backend.service.AdminService
import com.example.backend.service.RoleService
import com.example.backend.service.StudentService
import com.example.backend.utils.Response
import com.example.backend.utils.Status
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
@Validated
class AdminController {
    @Autowired
    lateinit var studentService: StudentService
    @Autowired
    lateinit var adminService: AdminService
    @Autowired
    lateinit var roleService: RoleService

    @GetMapping("/user", params = ["type"])
    fun findAll(@RequestParam("type") type: String,
                @RequestParam("size", defaultValue = "10") size: Int,
                @RequestParam("page", defaultValue = "0") page: Int): Response<Page<out User>> {
        if (type == "student") {
            return Response.Ok("all students", studentService.findAll(PageRequest.of(page, size)))
        } else {
            return Response.Err("unknown type", Page.empty())
        }
    }

    @PostMapping("/register")
    fun insertOne(@RequestBody @Valid data: RegisterAdminRequest): Response<Admin> {
        return Response.Ok("insert ok", adminService.insertOne(data))
    }

    @DeleteMapping("/user/{id}")
    @Throws(NoSuchUserException::class)
    fun deleteUser(@PathVariable id: Long): Response<Status> {
        studentService.deleteOne(id)
        return Response.Ok("delete ok", Status.Ok)
    }
}