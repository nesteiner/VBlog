package com.example.backend.request

import com.example.backend.model.Role
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

class RegisterUserRequest(
    @NotBlank(message = "name cannot be blank")
    @Length(min = 5, max = 255, message = "name length must in 5-64")
    val name: String,

    @NotBlank
    val passwordHash: String,

    @NotBlank
    @Length(min = 5, max = 255, message = "nick name must in 5-255")
    val nickname: String,

    @Email(message = "email pattern error")
    @NotBlank(message = "email cannot be blank")
    val email: String,

    @NotBlank(message = "userface cannot be blank")
    @Length(min = 5, max = 255, message = "userface length must in 5-255")
    val userface: String,

    @NotEmpty
    val roles: List<Role>
)