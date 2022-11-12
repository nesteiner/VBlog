package com.example.backend.controller

import com.example.backend.exception.NoSuchRoleException
import com.example.backend.model.Role
import com.example.backend.request.RegisterRoleRequest
import com.example.backend.service.RoleService
import com.example.backend.utils.Response
import com.example.backend.utils.Status
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/role")
@Validated
class RoleController {
    @Autowired
    lateinit var roleService: RoleService

    @GetMapping("/{id}")
    @Throws(NoSuchRoleException::class)
    fun findOne(@PathVariable id: Long): Response<Role> {
        val role = roleService.findOne(id)
        if (role == null) {
            throw NoSuchRoleException("no such role with id: ${id}")
        } else {
            return Response.Ok("this role", role)
        }
    }

    @GetMapping
    fun findAll(): Response<List<Role>> {
        return Response.Ok("all roles", roleService.findAll())
    }

    @PostMapping
    fun insertOne(@RequestBody @Valid request: RegisterRoleRequest, result: BindingResult): Response<Role> {
        return Response.Ok("insert ok", roleService.insertOne(request))
    }

    @DeleteMapping("/{id}")
    fun deleteOne(@PathVariable id: Long): Response<Status> {
        roleService.deleteOne(id)
        return Response.Ok("delete ok", Status.Ok)
    }

    @PutMapping
    fun updateOne(@RequestBody data: Role): Response<Role> {
        return Response.Ok("update ok", roleService.updateOne(data))
    }
}