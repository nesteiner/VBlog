package com.example.backend.request

import com.example.backend.model.Role
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

class RegisterRequest(
    @NotBlank(message = "username cannot be blank")
    @Length(min = 5, message = "username length must greater than 5")
    val username: String,
    @NotBlank(message = "roles cannot be blank")
    val roles: List<Role>,
    @NotBlank(message = "password cannot be blank")
    val passwordHash: String
) {
}