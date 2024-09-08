package com.devtutorial.richlinks

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform