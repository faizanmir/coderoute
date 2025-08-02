package com.ai.coderoute.aireviewservice.service

import com.ai.coderoute.aireviewservice.utils.PromptUtil.reviewSystemPromptTemplate
import com.ai.coderoute.aireviewservice.utils.ReviewParser
import com.ai.coderoute.models.ReviewFinding
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.beans.factory.annotation.Autowired

class LlmReviewService @Autowired constructor(private val chatClient: ChatClient) {

    private val logger = LoggerFactory.getLogger(LlmReviewService::class.java)

    fun reviewCode(filename : String, fileContent : String) : List<ReviewFinding> {
        val codeBlock = """
        # Input Code
        
        FILE_PATH: $filename
        ```kotlin
        $fileContent
        ```
        """.trimIndent()

        val userMessage = UserMessage(codeBlock)
        val systemMessage = reviewSystemPromptTemplate.createMessage()

        val prompt = Prompt(listOf(systemMessage, userMessage))

        logger.info("Sending prompt to AI for file: $filename")
        val response = chatClient.prompt(prompt).call().content()
        return ReviewParser.parse(response ?: "")
    }
}