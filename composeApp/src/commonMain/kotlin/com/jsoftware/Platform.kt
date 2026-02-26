package com.jsoftware

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform