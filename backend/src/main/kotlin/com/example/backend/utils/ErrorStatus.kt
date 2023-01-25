package com.example.backend.utils

enum class ErrorStatus(val code: Int) {
    NoTokenFound(1),
    TokenExpired(2),
    TokenParseError(3),
    BadRequest(4),
    ConstraintViolationError(5),
    RegisterError(6),
    NoSuchUser(7),
    UserNameNotFound(8),
    UserDisabled(9),
    UserNamePasswordError(10),
    InternalServerError(11),
    Unknown(12)
}