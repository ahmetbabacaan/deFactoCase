package com.babacan.defactocase.common

fun String.toSHA1(): String {
    val bytes = toByteArray()
    val sha1 = java.security.MessageDigest.getInstance("SHA-1")
    val digest = sha1.digest(bytes)
    return digest.joinToString("") { "%02x".format(it) }
}

