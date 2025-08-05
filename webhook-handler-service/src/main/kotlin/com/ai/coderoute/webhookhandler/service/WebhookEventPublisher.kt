package com.ai.coderoute.webhookhandler.service

import com.ai.coderoute.models.PullRequestReceivedEvent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class WebhookEventPublisher
    @Autowired
    constructor(private val kafkaTemplate: KafkaTemplate<String, PullRequestReceivedEvent>) {
        private val logger = LoggerFactory.getLogger(WebhookEventPublisher::class.java)
        private val topic = "pr-events"

        fun publish(event: PullRequestReceivedEvent) {
            try {
                logger.info("Publishing event to topic '$topic' for PR #${event.pullNumber} in ${event.owner}/${event.repo}")
                kafkaTemplate.send(topic, event)
            } catch (e: Exception) {
                logger.error("Failed to publish event for PR #${event.pullNumber}", e)
            }
        }
    }
