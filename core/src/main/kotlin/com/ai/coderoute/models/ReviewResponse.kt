package com.ai.coderoute.models

import com.fasterxml.jackson.annotation.JsonProperty

data class ReviewResponse(
    @JsonProperty("reviews")
    val reviews: List<ReviewFinding>,
)
