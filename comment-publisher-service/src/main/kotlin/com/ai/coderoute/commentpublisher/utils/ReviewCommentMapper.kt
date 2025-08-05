package com.ai.coderoute.commentpublisher.utils

import com.ai.coderoute.models.ReviewComment
import com.ai.coderoute.models.ReviewFinding

object ReviewCommentMapper {
    fun fromReview(finding: ReviewFinding?): ReviewComment? {
        if (finding == null) return null
        if (finding.lineNumber <= 0) {
            return null
        }

        val commentBody =
            buildString {
                appendLine("### ${getEmojiForSeverity(finding.severity)} **${finding.category}: ${finding.ruleId}**")
                appendLine("> ${finding.message}")
                appendLine()
                appendLine("**Suggestion:**")
                appendLine(finding.suggestion)
                appendLine()
                appendLine("---")
                append("_Severity: **${finding.severity}**_")
            }

        return ReviewComment(
            path = finding.filePath,
            line = finding.lineNumber,
            body = commentBody,
        )
    }

    private fun getEmojiForSeverity(severity: String): String {
        return when (severity.uppercase()) {
            "CRITICAL" -> "🚨"
            "HIGH" -> "🔥"
            "MEDIUM" -> "⚠️"
            "LOW" -> "💡"
            "INFO" -> "ℹ️"
            else -> "📝"
        }
    }
}
