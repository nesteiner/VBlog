package com.example.backend.service

import com.example.backend.exception.RegisterException
import com.example.backend.model.User
import com.example.backend.repository.UserRepository
import com.example.backend.request.RegisterRequest
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.User as OtherUser

@Service
class UserService: UserDetailsService {
    @Autowired
    lateinit var userRepository: UserRepository

    @Throws(RegisterException::class)
    fun insertOne(data: RegisterRequest): User {
        try {
            val user = User(null, data.username, data.passwordHash)
            return userRepository.save(user)
        } catch (exception: Exception) {
            throw RegisterException("username duplicate")
        }
    }

    fun deleteOne(id: Long) {
        userRepository.deleteById(id)
    }

    fun updateOne(data: User): User {
        return userRepository.save(data)
    }

    fun findOne(id: Long): User? {
        return userRepository.findByIdOrNull(id)
    }

    fun findOne(name: String): User? {
        return userRepository.findByName(name)
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val optionalUser = userRepository.findByName(username)
        return optionalUser?.let {user ->
            OtherUser(user.name, user.passwordHash, listOf())
        } ?: throw UsernameNotFoundException("no such user")
    }
}