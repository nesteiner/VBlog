package com.example.backend.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import org.hibernate.annotations.GenericGenerator
import java.sql.Timestamp

@Entity(name = "Category")
class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "myid")
    @GenericGenerator(name = "myid", strategy = "com.example.backend.generator.ManualInsertGenerator")
    val id: Long?,

    @Column(length = 64, nullable = false, unique = true)
    val name: String,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    val date: Timestamp = Timestamp(System.currentTimeMillis())
)

