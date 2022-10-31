package com.example.backend.exception

import java.lang.Exception

class LoginException(override val message: String): Exception(message) {
}