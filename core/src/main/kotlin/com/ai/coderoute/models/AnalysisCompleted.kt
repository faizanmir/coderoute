package com.ai.coderoute.models

data class AnalysisCompleted(
    val owner: String,
    val repo: String,
    val pullNumber: Int,
    val filename: String,
    val findings: List<ReviewFinding>,
)
