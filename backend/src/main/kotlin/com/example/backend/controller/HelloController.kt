package com.example.backend.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @GetMapping("/admin/hello")
    fun helloAdmin(): String {
        return "Hello Admin"
    }

    @GetMapping("/user/hello")
    fun helloUser(): String {
        return "Hello User"
    }
}