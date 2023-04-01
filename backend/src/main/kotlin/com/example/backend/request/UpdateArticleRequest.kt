package com.example.backend.request

import com.example.backend.model.Category
import com.example.backend.model.Tag
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import java.sql.Timestamp

class UpdateArticleRequest(
    @NotNull
    val id: Long,
    @NotBlank(message = "title cannot be blank")
    @Length(min = 5, max = 255, message = "title length must in 5-255")
    val title: String,
    @NotBlank(message = "markdown content cannot be blank")
    val markdownContent: String,
    @NotBlank(message = "html content cannot be blank")
    val htmlContent: String,
    @NotBlank(message = "summary cannot be blank")
    val summary: String,
    @NotNull(message = "category cannot be null")
    val category: Category,
    @NotNull(message = "tags cannot be null")
    val tags: List<Tag>
)