package com.example.backend.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import java.io.Serializable

@Entity(name = "ArticleTag")
@IdClass(ArticleTag.UPK::class)
class ArticleTag(
    @Id
    @Column(nullable = false)
    val articleid: Long,

    @Id
    @Column(nullable = false)
    val tagid: Long
) {
    class UPK(
        val articleid: Long,
        val tagid: Long
    ): Serializable {
        constructor(): this(0, 0)
    }
}