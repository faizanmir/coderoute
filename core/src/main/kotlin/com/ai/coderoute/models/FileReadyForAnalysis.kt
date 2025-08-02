package com.ai.coderoute.models

data class FileReadyForAnalysis(
    val owner: String,
    val repo: String,
    val pullNumber: Int,
    val filename: String,
    val contentWithLineNumbers: String
)