package com.ai.coderoute.models

import java.time.Instant

data class PullRequestReviewThreadEvent(
    val action: String,
    val threadId: Long,
    val owner: String,
    val repo: String,
    val pullNumber: Int,
    val resolvedBy: String?,
    val resolvedAt: Instant?,
)
