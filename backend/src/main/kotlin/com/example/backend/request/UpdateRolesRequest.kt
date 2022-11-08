package com.example.backend.request

import com.example.backend.model.Role
import org.hibernate.validator.constraints.Length

class UpdateRolesRequest(
    @Length(min = 1, message = "roles cannot be empty")
    val roles: List<Role>
) {
}