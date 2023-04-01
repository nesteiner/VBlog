package com.example.backend.repository

import com.example.backend.model.Tag
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TagRepository: JpaRepository<Tag, Long> {
    @Query("delete from ArticleTag at where at.articleid = ?1")
    @Transactional
    @Modifying
    fun deleteByArticleId(aid: Long): Int

    @Query("select t.id from Tag t where t.name in (?1)")
    fun findIdsByNames(names: Array<String>): List<Long>

    fun findByName(name: String): Tag?
}