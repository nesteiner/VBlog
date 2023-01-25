package com.example.backend.request

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

class LoginRequest(
    @NotBlank(message = "username cannot be blank")
    @Length(min = 5, message = "username length must greater than 5")
    val username: String,

    @NotBlank(message = "password cannot be blank")
    val passwordHash: String
)