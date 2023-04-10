package com.example.backend.controller

import com.example.backend.exception.NoSuchUserException
import com.example.backend.model.Article
import com.example.backend.model.Role
import com.example.backend.model.User
import com.example.backend.request.RegisterUserRequest
import com.example.backend.request.UpdateEnabledRequest
import com.example.backend.request.UpdateRolesRequest
import com.example.backend.request.UpdateStateRequest
import com.example.backend.service.ArticleService
import com.example.backend.service.RoleService
import com.example.backend.service.UserService
import com.example.backend.utils.Response
import com.example.backend.utils.Status
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
@Validated
class AdminController {
    @Autowired
    lateinit var roleService: RoleService
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var articleService: ArticleService

    @PostMapping("/register")
    fun insertUser(@RequestBody @Valid data: RegisterUserRequest): Response<User?> {
        // return Response.Ok("insert ok", userService.register(data))
        val ifuser = userService.register(data)
        if (ifuser == null) {
            return Response.Err("register failed", null)
        } else {
            return Response.Ok("register ok", ifuser)
        }
    }

    @DeleteMapping("/user/{id}")
    @Throws(NoSuchUserException::class)
    fun deleteUser(@PathVariable id: Long): Response<Status> {
        userService.deleteOne(id)
        return Response.Ok("delete ok", Status.Ok)
    }

    @GetMapping("/user", params = ["nickname"])
    fun findAllUsersByNickname(@RequestParam("nickname") nickname: String): Response<List<User>> {
        if (nickname == "") {
            return Response.Ok("all users", userService.findAll())
        } else {
            return Response.Ok("these users", userService.findAllByNichname(nickname))
        }
    }

    @GetMapping("/user/{id}")
    fun findUserById(@PathVariable id: Long): Response<User?> {
        val ifuser = userService.findOne(id)
        if (ifuser == null) {
            return Response.Err("no such user", null)
        } else {
            return Response.Ok("this user", ifuser)
        }
    }

    // get all roles
    @GetMapping("/roles")
    fun findAllRoles(): Response<List<Role>> {
        return Response.Ok("all roles", roleService.findAll())
    }

    @PutMapping("/user/enabled")
    fun updateUserEnabled(@RequestBody request: UpdateEnabledRequest): Response<Boolean> {
        val result = userService.updateEnabled(request.userid, request.enabled)
        return Response.Ok("update ok", result)
    }

    @PutMapping("/user/role")
    fun updateUserRoles(@RequestBody request: UpdateRolesRequest): Response<List<Role>> {
        val result = userService.updateRoles(request.userid, request.roleids)
        return Response.Ok("update ok", result)
    }


    @GetMapping("/article/all", params = ["page", "size"])
    fun findAllArticle(@RequestParam("page") page: Int, @RequestParam("size") size: Int): Response<Page<Article>> {
        val pageable = PageRequest.of(page, size)
        return Response.Ok("these articles", articleService.findAll(pageable))
    }
}