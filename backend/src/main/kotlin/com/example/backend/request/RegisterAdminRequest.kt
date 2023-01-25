package com.example.backend.request

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

class RegisterAdminRequest(
    @NotBlank(message = "name cannot be blank")
    @Length(min = 5, max = 16, message = "length of name must in 5-20")
    val name: String,
    @NotBlank(message = "password cannot be blank")
    val passwordHash: String
)