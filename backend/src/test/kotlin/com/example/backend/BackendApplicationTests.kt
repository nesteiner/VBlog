package com.example.backend

import com.example.backend.model.Role
import com.example.backend.model.User
import com.example.backend.repository.RoleRepository
import com.example.backend.repository.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BackendApplicationTests {
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var roleRepository: RoleRepository
    @Test
    fun contextLoads() {
    }

    @Test
    fun injectFakeData() {
        userRepository.deleteAll()
        roleRepository.deleteAll()

        val roles = listOf<Role>(
            Role(null, "user"),
            Role(null, "admin")
        )

        roleRepository.saveAll(roles)

        val users = listOf<User>(
            User(null, "hello", "5f4dcc3b5aa765d61d8327deb882cf99", listOf(roles[0])),
            User(null, "world", "5f4dcc3b5aa765d61d8327deb882cf99", listOf(roles[0])),
            User(null, "admin", "5f4dcc3b5aa765d61d8327deb882cf99", roles)
        )


        userRepository.saveAll(users)
    }
}
