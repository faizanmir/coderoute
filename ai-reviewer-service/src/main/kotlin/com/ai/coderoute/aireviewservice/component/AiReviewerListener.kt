package com.ai.coderoute.aireviewservice.component

import com.ai.coderoute.aireviewservice.service.LlmReviewService
import com.ai.coderoute.constants.Events
import com.ai.coderoute.models.AnalysisCompleted
import com.ai.coderoute.models.FileReadyForAnalysis
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class AiReviewerListener
    @Autowired
    constructor(
        val llmReviewService: LlmReviewService,
        val kafkaTemplate: KafkaTemplate<String, AnalysisCompleted>,
    ) {
        private val logger = LoggerFactory.getLogger(AiReviewerListener::class.java)
        private val resultsTopic = Events.Review.REVIEW_COMPLETE

        @KafkaListener(
            topics = [Events.PR.Analysis.COMPLETE],
            groupId = "ai-review-service-group",
        )
        fun consumeFileReadyForAnalysisEvent(event: FileReadyForAnalysis) {
            logger.info("Received event for {} analysis", event.filename)
            try {
                val res = llmReviewService.reviewCode(event.filename, event.contentWithLineNumbers)
                val resultEvent = AnalysisCompleted(event.owner, event.repo, event.pullNumber, event.filename, res)
                kafkaTemplate.send(resultsTopic, Events.Review.REVIEW_COMPLETE_KEY, resultEvent)
                logger.info("Successfully processed and published results for {}", event.filename)
            } catch (e: Exception) {
                logger.error("FATAL: Failed to process file analysis for event: ${event.filename}", e)
            }
        }
    }
