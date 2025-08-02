package com.ai.coderoute.pranalyzer.config

import com.ai.coderoute.models.PullRequestReceivedEvent
import com.ai.coderoute.pranalyzer.service.PullRequestProcessor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

@Configuration
class PRAnalyzerConfig @Autowired constructor(val pullRequestProcessor: PullRequestProcessor) {

    private val logger = LoggerFactory.getLogger(PRAnalyzerConfig::class.java)

    @Bean
    fun consumePullRequestEvent(): Consumer<PullRequestReceivedEvent> {
        return Consumer { event ->
            logger.info("PR-ANALYZER: Consumed event for PR #${event.pullNumber} in ${event.owner}/${event.repo}")
            try {
                pullRequestProcessor.process(event)
            } catch (e: Exception) {
                logger.error("PR-ANALYZER: Unhandled exception during processing of PR #${event.pullNumber}", e)
            }
        }
    }


}