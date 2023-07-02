package com.example.backend.controller

import com.example.backend.exception.NoSuchArticleException
import com.example.backend.exception.NoSuchUserException
import com.example.backend.model.Article
import com.example.backend.model.ArticleShortcut
import com.example.backend.request.RegisterArticleRequest
import com.example.backend.request.UpdateArticleRequest
import com.example.backend.service.ArticleService
import com.example.backend.service.UserService
import com.example.backend.utils.Response
import com.example.backend.utils.Status
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

import java.util.*

@RestController
@RequestMapping("/article")
@Validated
class ArticleController {
    @Autowired
    lateinit var articleService: ArticleService
    @Autowired
    lateinit var userService: UserService
    @PostMapping
    fun insertOne(@RequestBody @Valid article: RegisterArticleRequest, bindingResult: BindingResult): Response<Article> {
        return Response.Ok("insert ok", articleService.insertOne(article))
    }

    @GetMapping("/all", params = ["keywords"])
    fun findAllByState(
        @RequestParam("state", defaultValue = "${Article.PUBLISHED}") state: Int,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "6") size: Int,
        @RequestParam("keywords") keywords: String): Response<Page<ArticleShortcut>> {

        val pageable = PageRequest.of(page, size)
        val otheruser = SecurityContextHolder.getContext().authentication.principal as User
        val ifuser = userService.findOne(otheruser.username)

        if (ifuser == null) {
            throw NoSuchUserException("no such user ${otheruser.username}")
        } else {
            val result = articleService.findAllByState(state, ifuser.id!!, keywords, pageable)
            return Response.Ok("these article", result)
        }
    }

    @GetMapping("/all")
    fun findAllByState(
        @RequestParam("state", defaultValue = "${Article.PUBLISHED}") state: Int,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "6") size: Int
    ): Response<Page<ArticleShortcut>> {
        val pageable = PageRequest.of(page, size)
        val otheruser = SecurityContextHolder.getContext().authentication.principal as User
        val ifuser = userService.findOne(otheruser.username)

        if (ifuser == null) {
            throw NoSuchUserException("no such user ${otheruser.username}")
        } else {
            return Response.Ok("these article", articleService.findAllByState(state, ifuser.id!!, pageable))
        }
    }

    @GetMapping("/{id}")
    fun findOne(@PathVariable id: Long): Response<Article> {
        val ifarticle = articleService.findOne(id)

        return if (ifarticle == null) {
            throw NoSuchArticleException("no such article")
        } else {
            Response.Ok("this article", ifarticle)
        }
    }

    @PutMapping
    fun updateOne(@RequestBody @Valid request: UpdateArticleRequest, bindingResult: BindingResult): Response<Status> {
        articleService.updateOne(request)
        return Response.Ok("update ok", Status.Ok)
    }

    @PutMapping("/dustbin", params = ["id", "state"])
    fun updateState(@RequestParam("id") id: Long, @RequestParam("state") state: Int): Response<Status> {
        articleService.updateState(id, state)
        return Response.Ok("update ok", Status.Ok)
    }

    @DeleteMapping("/{id}")
    fun deleteOne(@PathVariable("id") id: Long): Response<Status> {
        articleService.updateState(id, 3)
        return Response.Ok("delete ok", Status.Ok)
    }

    @PutMapping("/restore/{id}")
    fun restore(@PathVariable id: Long): Response<Article> {
        val result = articleService.restore(id)
        return Response.Ok("restore ok", result)
    }
}