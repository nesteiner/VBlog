package com.example.backend.model

import java.sql.Timestamp

class ArticleShortcut(
    val id: Long,
    val title: String,
    val summary: String,
    val category: Category,
    val author: User,
    val publishDate: Timestamp,
    val editTime: Timestamp,
    val state: Int
)