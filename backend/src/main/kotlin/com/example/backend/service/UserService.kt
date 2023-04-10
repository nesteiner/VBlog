package com.example.backend.service

import com.example.backend.exception.NoSuchUserException
import com.example.backend.model.Role
import com.example.backend.model.User
import com.example.backend.model.UserRole
import com.example.backend.repository.ArticleRepository
import com.example.backend.repository.RoleRepository
import com.example.backend.repository.UserRepository
import com.example.backend.repository.UserRoleRepository
import com.example.backend.request.RegisterUserRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.sql.Timestamp
import org.springframework.security.core.userdetails.User as OtherUser

@Service
class UserService: UserDetailsService {
    companion object {
        const val REGISTER_OK = 0
        const val REGISTER_ERROR = 1
        const val OTHER_ERROR = 2
    }

    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var roleRepository: RoleRepository
    @Autowired
    lateinit var userRoleRepository: UserRoleRepository
    @Autowired
    lateinit var articleRepository: ArticleRepository
    override fun loadUserByUsername(username: String): UserDetails {
        val ifuser = userRepository.findByName(username)
        if (ifuser == null) {
            throw UsernameNotFoundException("no such user: ${username}")
        } else {
            val authorities = ifuser.roles.map {
                SimpleGrantedAuthority(it.name)
            }

            return OtherUser(ifuser.name, ifuser.passwordHash, authorities)
        }
    }

    fun register(data: RegisterUserRequest): User? {
        val ifuser = userRepository.findByName(data.name)
        if (ifuser != null) {
            return null
        } else {
            val user = User(
                null,
                data.name,
                data.passwordHash,
                data.nickname,
                true,
                data.email,
                data.userface,
                Timestamp(System.currentTimeMillis()),
                data.roles
            )

            return userRepository.save(user)
        }
    }

    fun updateEmail(id: Long, email: String): User? {
        val ifuser = userRepository.findByIdOrNull(id)
        if (ifuser != null) {
            ifuser.email = email
            userRepository.save(ifuser)
        }

        return ifuser
    }

    fun findAllByNichname(nickname: String): List<User> {
        return userRepository.findAllByNickname(nickname)
    }

    fun findAll(): List<User> {
        return userRepository.findAll()
    }

    fun updateEnabled(id: Long, enabled: Boolean): Boolean {
        val ifuser = userRepository.findByIdOrNull(id)
        if (ifuser != null) {
            ifuser.enabled = enabled
            userRepository.save(ifuser)
            return enabled
        } else {
            throw NoSuchUserException("no such user: ${id}")
        }
    }

    fun deleteOne(id: Long) {
        val user = userRepository.findByIdOrNull(id)
        if (user != null) {
            userRepository.deleteById(id)
            articleRepository.deleteAllByAuthor(user)
        }
    }

    fun updateRoles(id: Long, rids: Array<Long>): List<Role> {
        userRoleRepository.deleteAllByUserid(id)

        val ifuser = userRepository.findByIdOrNull(id)
        if (ifuser != null) {
            val userroles = ifuser.roles.map {
                UserRole(id, it.id!!)
            }

            userRoleRepository.deleteAll(userroles)

            ifuser.roles = rids.map {
                roleRepository.findByIdOrNull(it)!!
            }

            userRepository.save(ifuser)
            return ifuser.roles
        } else {
            throw NoSuchUserException("no such user: ${id}")
        }

    }

    fun findOne(id: Long): User? {
        return userRepository.findByIdOrNull(id)
    }

    fun findOne(name: String): User? {
        return userRepository.findByName(name)
    }

    fun updateOne(user: User): User {
        return userRepository.save(user)
    }
}