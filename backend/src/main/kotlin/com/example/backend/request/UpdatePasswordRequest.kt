package com.example.backend.request

import jakarta.validation.constraints.NotBlank

class UpdatePasswordRequest(
    @NotBlank(message = "password cannot be blank")
    val passwordHash: String
) {
}