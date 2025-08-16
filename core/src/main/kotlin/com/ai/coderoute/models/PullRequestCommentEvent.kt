package com.ai.coderoute.models

import java.time.Instant

data class PullRequestCommentEvent(
    val action: String,
    val commentId: Long,
    val owner: String,
    val repo: String,
    val pullNumber: Int,
    val author: String,
    val body: String?,
    val filePath: String?,
    val line: Int?,
    val inThread: Boolean,
    val updatedAt: Instant,
)
