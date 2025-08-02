package com.ai.coderoute.models

import com.fasterxml.jackson.annotation.JsonProperty

data class ReviewComment(
    val path: String,
    @JsonProperty("line")
    val line: Int,
    val body: String,
)

