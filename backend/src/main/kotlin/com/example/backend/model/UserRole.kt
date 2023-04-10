package com.example.backend.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import java.io.Serializable

@Entity(name = "UserRole")
@IdClass(UserRole.UPK::class)
class UserRole(
    @Id
    @Column(nullable = false)
    val userid: Long,

    @Id
    @Column(nullable = false)
    val roleid: Long
) {
    inner class UPK(
        val userid: Long,
        val roleid: Long
    ): Serializable {
        constructor(): this(0, 0)
    }
}