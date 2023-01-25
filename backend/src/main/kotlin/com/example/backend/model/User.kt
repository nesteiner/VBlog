package com.example.backend.model
interface User {
    val id: Long?
    val name: String
    val passwordHash: String
    val roles: List<Role>
}