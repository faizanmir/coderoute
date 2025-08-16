package com.ai.coderoute.webhookhandler.service

import com.ai.coderoute.constants.Events
import com.ai.coderoute.models.PullRequestCommentEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class CommentEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, PullRequestCommentEvent>,
) {
    private val logger = LoggerFactory.getLogger(CommentEventPublisher::class.java)
    private val topic = Events.Comment.RECEIVED

    fun publish(event: PullRequestCommentEvent) {
        try {
            kafkaTemplate.send(topic, Events.Comment.RECEIVED_KEY, event)
            logger.info("Published comment event {}", event.commentId)
        } catch (e: Exception) {
            logger.error("Failed to publish comment event {}", event.commentId, e)
        }
    }
}
