package com.example.backend

import com.example.backend.model.User
import com.example.backend.repository.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BackendApplicationTests {
    @Autowired
    lateinit var userRepository: UserRepository
    @Test
    fun contextLoads() {
    }

    @Test
    fun injectFakeData() {
        userRepository.deleteAll()

        val users = listOf<User>(
            User(null, "hello", "user", "password"),
            User(null, "world", "user", "password"),
            User(null, "admin", "admin,user", "password")
        )

        userRepository.saveAll(users)
    }
}
