package com.ai.coderoute.aireviewservice.service

import com.ai.coderoute.aireviewservice.component.JsonParser
import com.ai.coderoute.aireviewservice.utils.PromptUtil
import com.ai.coderoute.models.ReviewFinding
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class LlmReviewService @Autowired constructor(private val chatClient: ChatClient, private val jsonParser: JsonParser) {

    private val logger = LoggerFactory.getLogger(LlmReviewService::class.java)

    fun reviewCode(filename: String, fileContent: String): List<ReviewFinding?> {
        val codeBlock = """
            FILE_PATH: $filename
            NOTE:
            - Each line of code includes its **line number prefix** (e.g., `12 | val foo = "bar"`).
            - Use these line numbers in your analysis output.
            $fileContent
            """.trimIndent()

        val userMessage = UserMessage(codeBlock)
        val systemMessage = PromptUtil.reviewSystemMessage
        val response = chatClient
            .prompt()
            .messages(listOf(systemMessage, userMessage))
            .call()

        logger.info("Sending prompt to AI for file: ${response.content()}")
        return jsonParser.parseFindingList(response.content())
    }
}