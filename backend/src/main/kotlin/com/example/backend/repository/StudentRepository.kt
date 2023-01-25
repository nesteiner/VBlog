package com.example.backend.repository

import com.example.backend.model.Student
import org.springframework.data.jpa.repository.JpaRepository

interface StudentRepository: JpaRepository<Student, Long> {
    fun findByName(name: String): Student?
}