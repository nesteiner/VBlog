package com.example.backend.service

import com.example.backend.model.ImageItem
import com.example.backend.repository.ImageItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ImageItemService {
    @Autowired
    lateinit var imageItemRepository: ImageItemRepository

    fun insertOne(name: String, path: String): ImageItem {
        val imageitem = ImageItem(null, name, path)
        return imageItemRepository.save(imageitem)
    }

    fun deleteOne(id: Long) {
        imageItemRepository.deleteById(id)
    }

    fun findOne(id: Long): ImageItem? {
        return imageItemRepository.findByIdOrNull(id)
    }

    fun findAll(pageable: Pageable): Page<ImageItem> {
        return imageItemRepository.findAll(pageable)
    }

    fun findALl(): List<ImageItem> {
        return imageItemRepository.findAll()
    }
}