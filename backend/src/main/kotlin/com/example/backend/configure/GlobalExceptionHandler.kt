package com.example.backend.configure

import com.example.backend.exception.*
import com.example.backend.utils.Response
import jakarta.validation.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.io.IOException
import java.lang.StringBuilder
import java.sql.SQLIntegrityConstraintViolationException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(LoginException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleException(exception: LoginException): Response<Unit> {
        val message = exception.message
        return Response.Err(message)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(exception: ConstraintViolationException): Response<Unit> {
        val message = StringBuilder()
        val constraintViolations = exception.constraintViolations
        constraintViolations.forEach { constraintViolation ->
            val _message = constraintViolation.message
            message.append("[").append(_message).append("]")
        }

        return Response.Err(message.toString())
    }

    @ExceptionHandler(RegisterException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(exception: RegisterException): Response<Unit> {
        val message = exception.message
        return Response.Err(message)
    }

    @ExceptionHandler(NoSuchUserException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(exception: NoSuchUserException): Response<Unit> {
        val message = exception.message
        return Response.Err(message)
    }

    @ExceptionHandler(NoSuchArticleException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(exception: NoSuchArticleException): Response<Unit> {
        val message = exception.message
        return Response.Err(message)
    }

    // for username not found exception
    @ExceptionHandler(AuthenticationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(exception: AuthenticationException): Response<Unit> {
        val message = exception.message ?: "Username not found"
        return Response.Err(message)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(exception: DataIntegrityViolationException): Response<Unit> {
        val message = exception.message ?: "data integrity violation exception"
        return Response.Err(message)
    }

    @ExceptionHandler(UserNotEnabledException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(exception: UserNotEnabledException): Response<Unit> {
        val message = exception.message
        return Response.Err(message)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(exception: Exception): Response<Unit> {
        val message = exception.message ?: "Internal exception occurs"
        exception.printStackTrace()
        return Response.Err(message)
    }

    @ExceptionHandler(IOException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(exception: IOException): Response<Unit> {
        val message = exception.message ?: "Internal exception occurs"
        exception.printStackTrace()
        return Response.Err(message)
    }
}