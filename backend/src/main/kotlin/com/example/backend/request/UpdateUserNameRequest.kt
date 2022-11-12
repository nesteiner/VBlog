package com.example.backend.request

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

class UpdateUserNameRequest(
    val id: Long?,
    @NotBlank(message = "name cannot be blank")
    @Length(min = 5, message = "name length must greater than 5")
    val name: String
) {
}