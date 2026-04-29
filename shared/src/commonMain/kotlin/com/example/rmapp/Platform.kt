package com.example.rmapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform