package com.example.backend.model

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity(name = "ImageItem")
class ImageItem(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "myid")
    @GenericGenerator(name = "myid", strategy = "com.example.backend.generator.ManualInsertGenerator")
    val id: Long?,

    @Column(length = 64, nullable = false)
    val name: String,

    @Column(length = 128, nullable = false)
    val path: String
)