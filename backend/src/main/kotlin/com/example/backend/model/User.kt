package com.example.backend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*


@Entity(name = "User")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @Column(length = 64, nullable = false, unique = true)
    var name: String,
    @Column(length = 255, nullable = false)
    @JsonIgnore
    var passwordHash: String,
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "User_Role",
        joinColumns = [JoinColumn(name = "userid", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "roleid", referencedColumnName = "id")])
    var roles: List<Role>
    ){
}