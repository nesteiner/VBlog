package com.example.backend.configure

import com.example.backend.exception.LoginException
import com.example.backend.exception.NoSuchUserException
import com.example.backend.exception.RegisterException
import com.example.backend.utils.ErrorStatus
import com.example.backend.utils.Response
import jakarta.validation.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.StringBuilder
import java.sql.SQLIntegrityConstraintViolationException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(LoginException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleException(exception: LoginException): Response<Int> {
        val message = exception.message
        return Response.Err(message, exception.status.code)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(exception: ConstraintViolationException): Response<Int> {
        val message = StringBuilder()
        val constraintViolations = exception.constraintViolations
        constraintViolations.forEach { constraintViolation ->
            val _message = constraintViolation.message
            message.append("[").append(_message).append("]")
        }

        return Response.Err(message.toString(), ErrorStatus.ConstraintViolationError.code)
    }

    @ExceptionHandler(RegisterException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(exception: RegisterException): Response<Int> {
        val message = exception.message
        return Response.Err(message, ErrorStatus.RegisterError.code)
    }

    @ExceptionHandler(NoSuchUserException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(exception: NoSuchUserException): Response<Int> {
        val message = exception.message
        return Response.Err(message, ErrorStatus.NoSuchUser.code)
    }

    // for username not found exception
    @ExceptionHandler(AuthenticationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(exception: AuthenticationException): Response<Int> {
        val message = exception.message ?: "Username not found"
        return Response.Err(message, ErrorStatus.UserNameNotFound.code)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(exception: DataIntegrityViolationException): Response<Int> {
        val message = exception.message ?: "data integrity violation exception"
        return Response.Err(message, ErrorStatus.BadRequest.code)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(exception: Exception): Response<Int> {
        val message = exception.message ?: "Internal exception occurs"
        return Response.Err(message, ErrorStatus.InternalServerError.code)
    }
}