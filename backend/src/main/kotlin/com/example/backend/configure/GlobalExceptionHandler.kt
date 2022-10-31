package com.example.backend.configure

import com.example.backend.exception.LoginException
import com.example.backend.exception.RegisterException
import com.example.backend.utils.Response
import com.example.backend.utils.Status
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.StringBuilder

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(LoginException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleException(exception: LoginException): Response<Status> {
        val message = exception.message
        return Response.Err(message)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(exception: ConstraintViolationException): Response<Status> {
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
    fun handleException(exception: RegisterException): Response<Status> {
        val message = exception.message
        return Response.Err(message)
    }
}