package com.example.backend.controller

import com.example.backend.model.Category
import com.example.backend.request.RegisterCategoryRequest
import com.example.backend.service.CategoryService
import com.example.backend.utils.Response
import com.example.backend.utils.Status
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/category")
@Validated
class CategoryController {
    @Autowired
    lateinit var categoryService: CategoryService

    @GetMapping("/all")
    fun findAll(): Response<List<Category>> {
        return Response.Ok("all categories", categoryService.findAll())
    }

    @DeleteMapping(params = ["id"])
    fun deleteOne(@RequestParam("id") id: Long): Response<Status> {
        categoryService.deleteOne(id)
        return Response.Ok("delete ok", Status.Ok)
    }

    @PostMapping
    fun insertOne(@RequestBody @Valid request: RegisterCategoryRequest): Response<Category?> {
        return Response.Ok("insert ok", categoryService.insertOne(request))
    }

    @PutMapping
    fun updateOne(@RequestBody category: Category): Response<Category> {
        return Response.Ok("update ok", categoryService.updateOne(category))
    }
}