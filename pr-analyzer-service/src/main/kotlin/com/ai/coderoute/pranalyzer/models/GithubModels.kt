package com.ai.coderoute.pranalyzer.models

data class GitHubComparison(
    val files: List<ChangedFile> = emptyList(),
) {
    data class ChangedFile(
        val sha: String,
        val filename: String,
        val status: String,
    )
}

data class GitHubFileContent(
    val name: String,
    val path: String,
    val sha: String,
    val type: String,
    val content: String,
    val encoding: String,
)
