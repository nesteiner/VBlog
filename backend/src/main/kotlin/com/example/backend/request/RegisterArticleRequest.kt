package com.example.backend.request

import com.example.backend.model.Category
import com.example.backend.model.Tag
import com.example.backend.model.User
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

class RegisterArticleRequest(
    @NotBlank(message = "title cannot be empty")
    @Length(max = 255, message = "title length must less than 255")
    val title: String,

    @NotBlank(message = "markdown content cannot be empty")
    val markdownContent: String,

    @NotBlank(message = "html content cannot be empty")
    val htmlContent: String,

    @NotBlank(message = "summary cannot be emoty")
    var summary: String,

    @NotNull(message = "category cannot be null")
    val category: Category,

    @NotNull(message = "state cannot be null")
    val state: Int,

    @NotEmpty(message = "Tags cannot be empty")
    val tags: List<Tag>,
)