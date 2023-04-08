package com.example.backend.repository

import com.example.backend.model.Article
import com.example.backend.model.Category
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository: JpaRepository<Article, Long> {
    @Query("select a from Article a where a.state = ?1 and a.author.id = ?2 and a.title like concat('%', ?3, '%') order by a.editTime desc")
    fun findAllByState(state: Int, uid: Long, keywords: String, pageable: Pageable): Page<Article>

    @Query("select a from Article a where a.state = ?1 and a.author.id = ?2 order by a.editTime desc")
    fun findAllByState(state: Int, uid: Long, pageable: Pageable): Page<Article>

    @Query("select count(*) from Article a where a.state = ?1 and a.title like concat('%', ?2, '%')")
    fun findAllByState(state: Int, keywords: String, pageable: Pageable): Page<Article>

    @Query("select count(*) from Article a where a.state = ?1 and a.author.id = ?2 and a.title like concat('%', ?3, '%')")
    fun countAllByState(state: Int, uid: Long, keywords: String): Int


    @Query("select count(*) from Article a where a.state = ?1 and a.author.id = ?2")
    fun countAllByState(state: Int, aid: Long): Int

    @Query("select count(*) from Article a where a.state = ?1")
    fun countAllByState(state: Int): Int

    @Query("update Article a set a.pageView = a.pageView + 1 where a.id = ?1 ")
    @Transactional
    @Modifying
    fun pageViewIncrement(id: Long)

    fun findAllByCategory(category: Category): List<Article>
}