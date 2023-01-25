package com.example.backend

import com.example.backend.model.Admin
import com.example.backend.model.Role
import com.example.backend.repository.AdminRepository
import com.example.backend.repository.RoleRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UsernameNotFoundException

@SpringBootTest
class BackendApplicationTests {
    @Autowired
    lateinit var roleRepository: RoleRepository
    @Autowired
    lateinit var adminRepository: AdminRepository
    @Test
    fun contextLoads() {
    }

    @Test
    fun injectAdmin() {
        val roles = listOf<Role>(
            Role(1L, "student"),
            Role(2L, "admin")
        )

        roleRepository.saveAll(roles)

        val admin = Admin(null, "admin", "5f4dcc3b5aa765d61d8327deb882cf99", listOf(roles[1]))
        adminRepository.save(admin)
    }

    @Test
    fun testThrow() {
        try {
            throw UsernameNotFoundException("user name not found")
        } catch (exception: UsernameNotFoundException) {
            println(exception.message)
        }
    }
}
