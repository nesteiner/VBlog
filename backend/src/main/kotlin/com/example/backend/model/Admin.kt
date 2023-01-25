package com.example.backend.model

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity(name = "Admin")
class Admin(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "myid")
    @GenericGenerator(name = "myid", strategy = "com.example.backend.generator.ManualInsertGenerator")
    override val id: Long?,
    @Column(length = 16, nullable = false)
    override val name: String,
    @Column(length = 256, nullable = false)
    override val passwordHash: String,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "AdminRole",
        joinColumns = [JoinColumn(name = "userid", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "roleid", referencedColumnName = "id")]
    )
    override val roles: List<Role>
): User