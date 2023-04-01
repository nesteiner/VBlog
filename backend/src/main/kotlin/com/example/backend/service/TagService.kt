package com.example.backend.service

import com.example.backend.model.Tag
import com.example.backend.repository.TagRepository
import com.example.backend.request.RegisterTagRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TagService {
    @Autowired
    lateinit var tagRepository: TagRepository

    fun insertOne(data: RegisterTagRequest): Tag {
        val iftag = tagRepository.findByName(data.name)
        if (iftag == null) {
            val tag = Tag(null, data.name)
            return tagRepository.save(tag)
        } else {
            return iftag
        }
    }

    fun deleteOne(id: Long) {
        tagRepository.deleteById(id)
    }

    fun updateOne(data: Tag): Tag {
        val iftag = tagRepository.findByName(data.name)
        if (iftag == null) {
            return tagRepository.save(data)
        } else {
            return iftag
        }
    }

    fun findAll(): List<Tag> {
        return tagRepository.findAll()
    }

    fun findOne(id: Long): Tag? {
        return tagRepository.findByIdOrNull(id)
    }
}