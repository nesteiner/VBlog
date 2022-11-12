package com.example.backend.service

import com.example.backend.model.Role
import com.example.backend.repository.RoleRepository
import com.example.backend.request.RegisterRoleRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RoleService {
    companion object {
        private const val DEFAULT_NAME = "user"
    }

    @Autowired
    lateinit var roleRepository: RoleRepository

    fun insertOne(request: RegisterRoleRequest): Role {
        val data = Role(null, request.name)
        return roleRepository.save(data)
    }

    fun deleteOne(id: Long) {
        roleRepository.deleteById(id)
    }

    fun updateOne(data: Role): Role {
        return roleRepository.save(data)
    }

    fun findOne(id: Long): Role? {
        return roleRepository.findByIdOrNull(id)
    }

    fun findOne(name: String): Role? {
        return roleRepository.findByName(name)
    }

    fun findDefault(): Role {
        var role = findOne(DEFAULT_NAME)
        if (role == null) {
            role = Role(null, DEFAULT_NAME)
            role = roleRepository.save(role)
        }

        return role
    }
    fun findAll(): List<Role> {
        return roleRepository.findAll()
    }
}