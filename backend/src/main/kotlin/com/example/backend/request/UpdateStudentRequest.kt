package com.example.backend.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

class UpdateStudentRequest(
    @NotNull(message = "id cannot be null")
    val id: Long,

    @NotBlank(message = "name cannot be blank")
    @Length(min = 5, max = 20, message = "length of name must in 5-20")
    val name: String,

    @NotBlank(message = "grade cannot be blank")
    @Length(max = 4, message = "length of grade must less than 4")
    val grade: String,

    @NotBlank(message = "major cannot be blank")
    @Length(max = 20, message = "length of major must less than 20")
    val major: String,

    @NotBlank(message = "clazz cannot be blank")
    @Length(max = 10)
    val clazz: String,

    @NotBlank(message = "institude cannot be blank")
    @Length(max = 30, message = "length of institude must less than 30")
    val institude: String,

    @NotBlank(message = "telephone cannot blank")
    @Length(min = 11, max = 11, message = "length of telephone must be 11")
    val telephone: String,

    @NotBlank(message = "email cannot be blank")
    @Email(message = "email pattern error")
    val email: String,

    @NotBlank(message = "password cannot be blank")
    val passwordHash: String,

    @NotBlank(message = "cardId cannot be blank")
    @Length(max = 18, message = "length of cardId must less than 18")
    val cardId: String,

    @NotBlank(message = "sex cannot be blank")
    @Length(min = 2, max = 2, message = "sex must be 男 or 女")
    val sex: String,
)