package com.example.backend.exception

import com.example.backend.utils.ErrorStatus

class LoginException(override val message: String, val status: ErrorStatus): Exception(message)