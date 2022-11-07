package com.example.backend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity(name = "User")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @Column(length = 64, nullable = false, unique = true)
    var name: String,
    @Column(length = 16, nullable = false)
    var roles: String,
    @Column(length = 255, nullable = false)
    @JsonIgnore
    var passwordHash: String){
}