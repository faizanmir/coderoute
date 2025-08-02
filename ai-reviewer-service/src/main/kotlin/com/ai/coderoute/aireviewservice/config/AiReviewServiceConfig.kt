package com.ai.coderoute.aireviewservice.config

import com.ai.coderoute.aireviewservice.service.LlmReviewService
import com.ai.coderoute.models.AnalysisCompleted
import com.ai.coderoute.models.FileReadyForAnalysis
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate
import java.util.function.Consumer

@Configuration
class AiReviewServiceConfig @Autowired constructor(val llmReviewService: LlmReviewService, val kafkaTemplate : KafkaTemplate<String, AnalysisCompleted>){

    private val logger = LoggerFactory.getLogger(AiReviewServiceConfig::class.java)
    private val resultsTopic = "review-results-events"

    @Bean
    fun consumeFileReadyForAnalysisEvent() : Consumer<FileReadyForAnalysis> {
        return Consumer<FileReadyForAnalysis> { event ->
            logger.info("Received event for {} analysis ", event.filename)
            val res = llmReviewService.reviewCode(event.filename, event.contentWithLineNumbers)
            val resultEvent = AnalysisCompleted(event.owner, event.repo, event.pullNumber, event.filename,res)
            kafkaTemplate.send(resultsTopic, resultEvent)
        }
    }
}