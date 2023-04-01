package com.example.backend.repository

import com.example.backend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findAllByNickname(nickname: String): List<User>
    fun findByName(name: String): User?
}