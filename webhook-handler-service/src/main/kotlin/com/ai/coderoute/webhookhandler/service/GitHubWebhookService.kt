package com.ai.coderoute.webhookhandler.service

import com.ai.coderoute.models.PullRequestReceivedEvent
import com.ai.coderoute.webhookhandler.models.GitHubPullRequestPayload
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GitHubWebhookService
    @Autowired
    constructor(
        private val objectMapper: ObjectMapper,
        val eventPublisher: WebhookEventPublisher,
    ) {
        private val logger = LoggerFactory.getLogger(GitHubWebhookService::class.java)

        fun convertJsonNodeToPayload(node: JsonNode): GitHubPullRequestPayload {
            return objectMapper.treeToValue(node, GitHubPullRequestPayload::class.java)
        }

        fun handlePing(payload: JsonNode) {
            println("Ping received: ${payload["zen"]}")
        }

        fun handlePullRequest(payload: JsonNode) {
            handlePullRequestInternal(jsonNode = payload)
        }

        private fun handlePullRequestInternal(jsonNode: JsonNode) {
            val payload = convertJsonNodeToPayload(jsonNode)
            logger.info("Received webhook for action '${payload.action}' on PR #${payload.pullRequest.number}")
            if (payload.action !in listOf("opened", "synchronize")) {
                logger.info("Ignoring action '${payload.action}' as it is not relevant for a code review trigger.")
                return
            }
            val event =
                PullRequestReceivedEvent(
                    owner = payload.repository.owner.login,
                    repo = payload.repository.name,
                    pullNumber = payload.pullRequest.number,
                    cloneUrl = payload.repository.cloneUrl,
                    baseSha = payload.pullRequest.base.sha,
                    headSha = payload.pullRequest.head.sha,
                )
            eventPublisher.publish(event)
        }

    fun handlePush(jsonNode: JsonNode) {
        val before = jsonNode.get("before").asText()
        val after = jsonNode.get("after").asText()
    }

}
