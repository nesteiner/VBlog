package com.example.backend.repository

import com.example.backend.model.UserRole
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository

@Repository
interface UserRoleRepository: JpaRepository<UserRole, UserRole.UPK> {
    @Transactional
    @Modifying
    fun deleteAllByUserid(userid: Long)
}