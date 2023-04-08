package com.example.backend.service

import com.example.backend.model.Category
import com.example.backend.repository.ArticleRepository
import com.example.backend.repository.CategoryRepository
import com.example.backend.request.RegisterCategoryRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class CategoryService {
    @Autowired
    lateinit var categoryRepository: CategoryRepository
    @Autowired
    lateinit var articleRepository: ArticleRepository
    fun findAll(): List<Category> {
        return categoryRepository.findAll()
    }

    fun deleteOne(id: Long) {
        val category = categoryRepository.findByIdOrNull(id)
        if (category == null) {
            throw Exception("no such category: ${id}")
        }

        var ifdefault = categoryRepository.findByName("default")
        if (ifdefault == null) {
            ifdefault = categoryRepository.save(Category(null, "default"))
        }

        val articles = articleRepository.findAllByCategory(category)
        articles.forEach {
            it.category = ifdefault
            articleRepository.save(it)
        }

        categoryRepository.deleteById(id)
    }

    fun updateOne(category: Category): Category {
        return categoryRepository.save(category)
    }

    fun insertOne(request: RegisterCategoryRequest): Category? {
        val ifcategory = categoryRepository.findByName(request.name)
        if (ifcategory == null) {
            val category = Category(null, request.name, Timestamp(System.currentTimeMillis()))
            return categoryRepository.save(category)
        } else {
            return null
        }

    }
}