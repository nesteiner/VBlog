package com.example.backend.repository

import com.example.backend.model.ArticleTag
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository

@Repository
interface ArticleTagRepository: JpaRepository<ArticleTag, ArticleTag.UPK> {
    @Transactional
    @Modifying
    fun deleteAllByArticleid(articleid: Long)
}