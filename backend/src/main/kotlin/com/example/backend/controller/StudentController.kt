package com.example.backend.controller

import com.example.backend.exception.NoSuchUserException
import com.example.backend.model.Student
import com.example.backend.request.RegisterStudentRequest
import com.example.backend.request.UpdateStudentRequest
import com.example.backend.service.StudentService
import com.example.backend.utils.Response
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import kotlin.jvm.Throws

@RestController
@RequestMapping("/student")
@Validated
class StudentController {
    @Autowired
    lateinit var studentService: StudentService

    @PostMapping("/register")
    fun insertOne(@RequestBody @Valid data: RegisterStudentRequest, bindingResult: BindingResult): Response<Student> {
        return Response.Ok("insert ok", studentService.insertOne(data))
    }

    @PutMapping
    fun updateOne(@RequestBody @Valid data: UpdateStudentRequest, bindingResult: BindingResult): Response<Student> {
        return Response.Ok("update ok", studentService.updateOne(data))
    }

    @GetMapping(params = ["page", "size"])
    fun findAll(@RequestParam("page") page: Int, @RequestParam("size") size: Int): Response<Page<Student>> {
        val pageable = PageRequest.of(page, size)
        val data = studentService.findAll(pageable)
        return Response.Ok("all students of this page", data)
    }

    @GetMapping("/{id}")
    @Throws(NoSuchUserException::class)
    fun findOne(@PathVariable("id") id: Long): Response<Student> {
        val data = studentService.findOne(id)
        if (data == null) {
            throw NoSuchUserException("no such student")
        } else {
            return Response.Ok("this student", data)
        }
    }
}