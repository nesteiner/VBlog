package com.example.backend.utils

data class Response<T>(val code: Status, val message: String, val data: T?) {
    companion object {
        @JvmStatic
        fun <T> Ok(message: String, data: T): Response<T> {
            return Response(Status.Ok, message, data)
        }

        @JvmStatic
        fun <T> Err(message: String, data: T): Response<T> {
            return Response(Status.Err, message, data)
        }
    }
}