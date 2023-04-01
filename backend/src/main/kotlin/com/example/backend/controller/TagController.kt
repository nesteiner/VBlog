package com.example.backend.controller

import com.example.backend.model.Tag
import com.example.backend.request.RegisterTagRequest
import com.example.backend.service.TagService
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
@RequestMapping("/tag")
@Validated
class TagController {
    @Autowired
    lateinit var tagService: TagService
    @GetMapping("/{id}")
    fun findOne(@PathVariable id: Long): Response<Tag?> {
        return Response.Ok("this tag", tagService.findOne(id))
    }

    @PostMapping
    fun insertOne(@RequestBody @Valid request: RegisterTagRequest, bindingResult: BindingResult): Response<Tag> {
        return Response.Ok("insert ok", tagService.insertOne(request))
    }

    @PutMapping
    fun updateOne(@RequestBody requset: Tag): Response<Tag> {
        return Response.Ok("update ok", tagService.updateOne(requset))
    }

    @DeleteMapping("/{id}")
    fun deleteOne(@PathVariable id: Long): Response<Status> {
        tagService.deleteOne(id)
        return Response.Ok("delete ok", Status.Ok)
    }
}