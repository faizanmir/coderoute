package com.ai.coderoute.models

data class CreateReviewRequest(
    val body: String,
    val event: String = "COMMENT",
    val comments: List<ReviewComment>,
)
