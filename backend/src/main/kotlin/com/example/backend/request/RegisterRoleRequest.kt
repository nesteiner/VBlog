package com.example.backend.request

import jakarta.validation.constraints.NotBlank

class RegisterRoleRequest(
    @NotBlank(message = "role name cannot be empty")
    val name: String
)