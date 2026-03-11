package com.linkit.company

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform