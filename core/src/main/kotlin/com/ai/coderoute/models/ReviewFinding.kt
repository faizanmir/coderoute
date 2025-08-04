package com.ai.coderoute.models

import com.fasterxml.jackson.annotation.JsonProperty

data class ReviewFinding(
    @JsonProperty("filePath")
    val filePath: String,
    @JsonProperty("lineNumber")
    val lineNumber: Int = 0,
    @JsonProperty("severity")
    val severity: String,
    @JsonProperty("category")
    val category: String,
    @JsonProperty("ruleId")
    val ruleId: String,
    @JsonProperty("message")
    val message: String,
    @JsonProperty("suggestion")
    val suggestion: String,
)
