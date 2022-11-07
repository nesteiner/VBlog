package com.example.backend.service

import com.example.backend.exception.RegisterException
import com.example.backend.model.User
import com.example.backend.repository.UserRepository
import com.example.backend.request.RegisterRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.authority.SimpleGrantedAuthority
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
            val user = User(null, data.username, data.roles, data.passwordHash)
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
            val simpleGrantedAuthorities = user.roles.split(",").map {
                SimpleGrantedAuthority("ROLE_" + it)
            }

            OtherUser(user.name, user.passwordHash, simpleGrantedAuthorities)
        } ?: throw UsernameNotFoundException("no such user")
    }
}