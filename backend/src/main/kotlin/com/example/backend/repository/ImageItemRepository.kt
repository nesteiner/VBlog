package com.example.backend.repository

import com.example.backend.model.ImageItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageItemRepository: JpaRepository<ImageItem, Long>