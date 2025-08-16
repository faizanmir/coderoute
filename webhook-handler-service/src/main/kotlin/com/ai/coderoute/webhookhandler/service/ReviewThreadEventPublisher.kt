package com.ai.coderoute.webhookhandler.service

import com.ai.coderoute.constants.Events
import com.ai.coderoute.models.PullRequestReviewThreadEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class ReviewThreadEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, PullRequestReviewThreadEvent>,
) {
    private val logger = LoggerFactory.getLogger(ReviewThreadEventPublisher::class.java)
    private val topic = Events.ReviewThread.RECEIVED

    fun publish(event: PullRequestReviewThreadEvent) {
        try {
            kafkaTemplate.send(topic, Events.ReviewThread.RECEIVED_KEY, event)
            logger.info("Published review thread event {}", event.threadId)
        } catch (e: Exception) {
            logger.error("Failed to publish review thread event {}", event.threadId, e)
        }
    }
}
