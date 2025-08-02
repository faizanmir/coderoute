package com.ai.coderoute.aireviewservice.utils

import com.ai.coderoute.models.ReviewFinding

object ReviewParser {
    fun parse(rawOutput: String): List<ReviewFinding> {
        val trimmedInput = rawOutput.trim()
        if (trimmedInput.isBlank() || trimmedInput.equals("NO_ISSUES_FOUND", ignoreCase = true)) {
            return emptyList()
        }

        return trimmedInput.split("\n---\n")
            .filter { it.isNotBlank() }
            .map { block ->
                val properties =
                    block.lines()
                        .filter { it.contains(':') }
                        .associate { line ->
                            val parts = line.split(':', limit = 2)
                            val key = parts[0].trim().uppercase()
                            val value = parts[1].trim()
                            key to value
                        }
                ReviewFinding(
                    filePath = properties["FILE_PATH"] ?: "Unknown File",
                    lineNumber = properties["LINE_NUMBER"]?.toIntOrNull(),
                    severity = properties["SEVERITY"] ?: "Info",
                    category = properties["CATEGORY"] ?: "General",
                    ruleId = properties["RULE_ID"] ?: "N/A",
                    message = properties["MESSAGE"] ?: "No message provided.",
                    suggestion = properties["SUGGESTION"] ?: "No suggestion provided.",
                    owasp = properties["OWASP"], // Becomes null if not found
                    cwe = properties["CWE"], // Becomes null if not found
                )
            }
    }
}
