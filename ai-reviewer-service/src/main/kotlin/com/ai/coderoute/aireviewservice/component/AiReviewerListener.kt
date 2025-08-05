package com.ai.coderoute.aireviewservice.component

import com.ai.coderoute.aireviewservice.service.LlmReviewService
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
        private val resultsTopic = "review-results-events"

        @KafkaListener(
            topics = ["file-analysis-events"],
            groupId = "ai-review-service-group",
        )
        fun consumeFileReadyForAnalysisEvent(event: FileReadyForAnalysis) {
            logger.info("Received event for {} analysis ", event.filename)
            val res = llmReviewService.reviewCode(event.filename, event.contentWithLineNumbers)
            val resultEvent = AnalysisCompleted(event.owner, event.repo, event.pullNumber, event.filename, res)
            kafkaTemplate.send(resultsTopic, resultEvent)
        }
    }
