package com.example.backend.service

import com.example.backend.model.Admin
import com.example.backend.repository.AdminRepository
import com.example.backend.repository.RoleRepository
import com.example.backend.request.RegisterAdminRequest
import com.example.backend.request.UpdateAdminRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.User as OtherUser

@Service
class AdminService: UserDetailsService, UserService<Admin, RegisterAdminRequest, UpdateAdminRequest> {
    @Autowired
    lateinit var adminRepository: AdminRepository
    @Autowired
    lateinit var roleRepository: RoleRepository

    override fun loadUserByUsername(username: String): UserDetails {
        val optionalUser = adminRepository.findByName(username)
        return optionalUser?.let {user ->
            val simpleGrantedAuthorities = user.roles.map {
                SimpleGrantedAuthority(it.name)
            }

            OtherUser(user.name, user.passwordHash, simpleGrantedAuthorities)
        } ?: throw UsernameNotFoundException("no such user")
    }

    override fun findOne(id: Long): Admin? {
        return adminRepository.findByIdOrNull(id)
    }

    override fun findOne(name: String): Admin? {
        return adminRepository.findByName(name)
    }

    override fun findAll(): List<Admin> {
        return adminRepository.findAll()
    }

    override fun findAll(pageable: Pageable): Page<Admin> {
        return adminRepository.findAll(pageable)
    }

    override fun deleteOne(id: Long) {
        val ifadmin = adminRepository.findByIdOrNull(id)
        ifadmin?.let {
            if (!(it.name == "admin")) {
                adminRepository.deleteById(id)
            }
        }
    }

    override fun updateOne(data: UpdateAdminRequest): Admin {
        val role = roleRepository.findByName("admin")!!
        return adminRepository.save(Admin(data.id, data.name, data.passwordHash, listOf(role)))
    }

    override fun insertOne(data: RegisterAdminRequest): Admin {
        val role = roleRepository.findByName("admin")!!
        return adminRepository.save(Admin(null, data.name, data.passwordHash, listOf(role)))
    }
}