package com.example.backend.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UserService<EntityType, RegisterType, UpdateType> {
    fun findOne(id: Long): EntityType?
    fun findOne(name: String): EntityType?
    fun findAll(): List<EntityType>
    fun findAll(pageable: Pageable): Page<EntityType>
    fun insertOne(data: RegisterType): EntityType
    fun updateOne(data: UpdateType): EntityType
    fun deleteOne(id: Long)
}