package com.example.backend.encoder

import com.example.backend.utils.MD5Util
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class MD5PasswordEncoder: PasswordEncoder {
    @Autowired
    lateinit var mD5Util: MD5Util

    override fun encode(rawPassword: CharSequence?): String {
        return mD5Util.encode(rawPassword as String)
    }

    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean {
        return encodedPassword == encode(rawPassword)
    }
}