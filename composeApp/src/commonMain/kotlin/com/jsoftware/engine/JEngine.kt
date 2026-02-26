package com.jsoftware.engine

interface JEngine {
    fun eval(input: String): JResult
    fun reset()
    fun shutdown()
}

data class JResult(
    val output: String,
    val isError: Boolean,
)
