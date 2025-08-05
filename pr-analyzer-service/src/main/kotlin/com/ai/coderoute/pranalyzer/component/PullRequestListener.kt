package com.ai.coderoute.pranalyzer.component

import com.ai.coderoute.models.PullRequestReceivedEvent
import com.ai.coderoute.pranalyzer.service.PullRequestProcessor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class PullRequestListener
    @Autowired
    constructor(val pullRequestProcessor: PullRequestProcessor) {
        private val logger = LoggerFactory.getLogger(PullRequestListener::class.java)

        @KafkaListener(
            topics = ["pr-events"],
            groupId = "pr-analyzer-group",
        )
        fun consumePullRequestEvent(event: PullRequestReceivedEvent) {
            logger.info("PR-ANALYZER: Consumed event for PR #${event.pullNumber} in ${event.owner}/${event.repo}")
            try {
                pullRequestProcessor.process(event)
            } catch (e: Exception) {
                logger.error("PR-ANALYZER: Unhandled exception during processing of PR #${event.pullNumber}", e)
            }
        }
    }
