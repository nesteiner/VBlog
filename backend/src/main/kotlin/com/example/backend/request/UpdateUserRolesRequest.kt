package com.example.backend.request

import com.example.backend.model.Role
import jakarta.validation.constraints.Size

class UpdateUserRolesRequest(
    val id: Long,
    @Size(min = 1, message = "roles must have less than 1 role")
    val roles: List<Role>
) {
}