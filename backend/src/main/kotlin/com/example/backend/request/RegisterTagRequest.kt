package com.example.backend.request

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

class RegisterTagRequest(
    @NotBlank(message = "name cannot be blank")
    @Length(min = 5, max = 64, message = "name must in 5-64")
    val name: String
)