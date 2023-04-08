package com.example.backend.model

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.sql.Timestamp

@Entity(name = "Article")
class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "myid")
    @GenericGenerator(name = "myid", strategy = "com.example.backend.generator.ManualInsertGenerator")
    val id: Long?,

    @Column(length = 255, nullable = false)
    var title: String,

    @Lob
    @Column(columnDefinition = "text", nullable = false)
    var markdownContent: String,

    @Lob
    @Column(columnDefinition = "text", nullable = false)
    var htmlContent: String,

    @Lob
    @Column(columnDefinition = "text", nullable = false)
    var summary: String,

    @OneToOne
    @JoinColumn(name = "cid", referencedColumnName = "id")
    var category: Category,

    @ManyToOne
    @JoinColumn(name = "aid", referencedColumnName = "id")
    val author: User,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    val publishDate: Timestamp,

    @Temporal(TemporalType.TIMESTAMP)
    var editTime: Timestamp,

    @Column(nullable = false)
    var state: Int,

    @Column(nullable = false)
    val pageView: Int,

    @ManyToMany
    @JoinTable(
        name = "ArticleTag",
        joinColumns = [JoinColumn(name = "articleid", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "tagid", referencedColumnName = "id")]
    )
    var tags: List<Tag>,
) {
    companion object {
        const val SCRATCH = 0
        const val PUBLISHED = 1
        const val DUSTBIN = 2
        const val DELETED = 3
    }
}