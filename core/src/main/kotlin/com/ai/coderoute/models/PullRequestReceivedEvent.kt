package com.ai.coderoute.models

data class PullRequestReceivedEvent(
    val owner: String,
    val repo: String,
    val pullNumber: Int,
    val cloneUrl: String,
    val baseSha: String,
    val headSha: String,
)
