package com.example.backend.utils

import org.springframework.stereotype.Component
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.security.MessageDigest

@Component
class MD5Util {
    fun encode(password: String): String {
        var md5: MessageDigest? = null
        try {
            md5 = MessageDigest.getInstance("MD5")
        } catch (exception: Exception) {
            throw RuntimeException(exception)
        }

        val charArray = password.toCharArray()
        val byteArray = ByteArray(charArray.size)

        for (start in 0..charArray.size - 1) {
            byteArray[start] = charArray[start].code.toByte()
        }

        val md5bytes = md5?.digest(byteArray)
        val hexValue = StringBuilder()

        for(md5byte in md5bytes ?: ByteArray(0)) {
            val value = md5byte.toInt() and 0xff
            if(value < 16) {
                hexValue.append("0")
            }

            hexValue.append(Integer.toHexString(value))
        }

        return hexValue.toString()
    }
}