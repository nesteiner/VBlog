package com.example.backend.repository

import com.example.backend.model.Admin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdminRepository: JpaRepository<Admin, Long> {
    fun findByName(name: String): Admin?
}