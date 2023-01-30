package com.example.backend.repository

import com.example.backend.model.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository: JpaRepository<Student, Long> {
    fun findByName(name: String): Student?
}