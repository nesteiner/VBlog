package com.example.backend.service

import com.example.backend.exception.NoSuchUserException
import com.example.backend.model.Article
import com.example.backend.model.ArticleTag
import com.example.backend.repository.ArticleRepository
import com.example.backend.repository.ArticleTagRepository
import com.example.backend.repository.TagRepository
import com.example.backend.repository.UserRepository
import com.example.backend.request.RegisterArticleRequest
import com.example.backend.request.UpdateArticleRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class ArticleService {
    @Autowired
    lateinit var articleRepository: ArticleRepository
    @Autowired
    lateinit var articleTagRepository: ArticleTagRepository
    @Autowired
    lateinit var userRepository: UserRepository

    fun insertOne(data: RegisterArticleRequest): Article {
        val timestamp = Timestamp(System.currentTimeMillis())
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val ifuser = userRepository.findByName(user.username)
        if (ifuser == null) {
            throw NoSuchUserException("no such user ${user.username}")
        } else {
            val article = Article(
                null, data.title, data.markdownContent, data.htmlContent, data.summary,
                data.category, ifuser, timestamp, timestamp, Article.PUBLISHED, 0, data.tags
            )
            return articleRepository.save(article)
        }
    }

    fun updateOne(article: UpdateArticleRequest): Article {
        val ifarticle = articleRepository.findByIdOrNull(article.id)
        if (ifarticle == null) {
            throw Exception("no such article: ${article.id}")
        } else {
            val timestamp = Timestamp(System.currentTimeMillis())
            ifarticle.editTime = timestamp
            ifarticle.markdownContent = article.markdownContent
            ifarticle.htmlContent = article.htmlContent
            // ifarticle.tags = article.tags
            ifarticle.category = article.category
            ifarticle.title = article.title
            ifarticle.summary = article.summary


            val ids = ifarticle.tags.map {
                ArticleTag.UPK(ifarticle.id!!, it.id!!)
            }

            ids.forEach {
                articleTagRepository.deleteById(it)
            }

            ifarticle.tags = article.tags

            return articleRepository.save(ifarticle)
        }
    }

    fun findAllByState(state: Int, userid: Long, keywords: String, pageable: Pageable): Page<Article> {
        return articleRepository.findAllByState(state, userid, keywords, pageable)
    }

    fun findAllByState(state: Int, userid: Long, pageable: Pageable): Page<Article> {
        return articleRepository.findAllByState(state, userid, pageable)
    }

    fun findAllByState(state: Int, keywords: String, pageable: Pageable): Page<Article> {
        return articleRepository.findAllByState(state, keywords, pageable)
    }

    fun findAll(pageable: Pageable): Page<Article> {
        return articleRepository.findAll(pageable)
    }

    fun updateState(articleId: Long, state: Int) {
        if (state == Article.DELETED) {
            articleRepository.deleteById(articleId)
        } else {
            val ifarticle = articleRepository.findByIdOrNull(articleId)
            if (ifarticle == null) {
                throw Exception("no such article: ${articleId}")
            } else {
                ifarticle.state = state
                articleRepository.save(ifarticle)
            }
        }
    }

    fun restore(aid: Long): Article {
        val ifarticle = articleRepository.findByIdOrNull(aid)
        if (ifarticle != null) {
            ifarticle.state = Article.PUBLISHED
            return articleRepository.save(ifarticle)
        } else {
            throw Exception("no such article: ${aid}")
        }
    }

    fun findOne(id: Long): Article? {
        val article = articleRepository.findByIdOrNull(id)
        if (article != null) {
            articleRepository.pageViewIncrement(id)
        }

        return article
    }
}