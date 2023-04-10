package com.example.backend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.sql.Timestamp

@Entity(name = "User")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "myid")
    @GenericGenerator(name = "myid", strategy = "com.example.backend.generator.ManualInsertGenerator")
    val id: Long?,

    @Column(length = 255, nullable = false, unique = true)
    val name: String,

    @Column(length = 255, nullable = false)
    @JsonIgnore
    val passwordHash: String,

    @Column(length = 255, nullable = false)
    val nickname: String,

    @Column(nullable = false)
    var enabled: Boolean,

    @Column(length = 255, nullable = false)
    var email: String,

    @Column(length = 255, nullable = false)
    var userface: String,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    val registerTime: Timestamp,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "UserRole",
        joinColumns = [JoinColumn(name = "userid", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "roleid", referencedColumnName = "id")]
    )
    var roles: List<Role>
)