package com.ai.coderoute.webhookhandler.service

import com.ai.coderoute.models.PullRequestReceivedEvent
import com.ai.coderoute.models.PullRequestCommentEvent
import com.ai.coderoute.models.PullRequestReviewThreadEvent
import com.ai.coderoute.webhookhandler.models.GitHubIssueCommentPayload
import com.ai.coderoute.webhookhandler.models.GitHubPullRequestPayload
import com.ai.coderoute.webhookhandler.models.GitHubReviewCommentPayload
import com.ai.coderoute.webhookhandler.models.GitHubReviewThreadPayload
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class GitHubWebhookService
    @Autowired
    constructor(
        private val objectMapper: ObjectMapper,
        val eventPublisher: WebhookEventPublisher,
        private val commentEventPublisher: CommentEventPublisher,
        private val reviewThreadEventPublisher: ReviewThreadEventPublisher,
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

        fun handlePullRequestReviewComment(payload: JsonNode) {
            val event = objectMapper.treeToValue(payload, GitHubReviewCommentPayload::class.java)
            val threadId = event.comment.inReplyToId ?: event.comment.id
            val commentEvent =
                PullRequestCommentEvent(
                    action = event.action,
                    commentId = event.comment.id,
                    owner = event.repository.owner.login,
                    repo = event.repository.name,
                    pullNumber = event.pullRequest.number,
                    author = event.comment.user.login,
                    body = event.comment.body,
                    filePath = event.comment.path,
                    line = event.comment.line,
                    threadId = threadId,
                    inThread = event.comment.inReplyToId != null,
                    updatedAt = Instant.parse(event.comment.updatedAt),
                )
            commentEventPublisher.publish(commentEvent)
        }

        fun handlePullRequestReviewThread(payload: JsonNode) {
            val event = objectMapper.treeToValue(payload, GitHubReviewThreadPayload::class.java)
            if (event.action !in listOf("resolved", "unresolved", "reopened")) {
                return
            }
            val threadEvent =
                PullRequestReviewThreadEvent(
                    action = event.action,
                    threadId = event.thread.id,
                    owner = event.repository.owner.login,
                    repo = event.repository.name,
                    pullNumber = event.pullRequest.number,
                    resolvedBy = event.thread.resolvedBy?.login,
                    resolvedAt = event.thread.resolvedAt?.let { Instant.parse(it) },
                )
            reviewThreadEventPublisher.publish(threadEvent)
        }

        fun handleIssueComment(payload: JsonNode) {
            val event = objectMapper.treeToValue(payload, GitHubIssueCommentPayload::class.java)
            if (event.issue.pullRequest == null) {
                return
            }
            val commentEvent =
                PullRequestCommentEvent(
                    action = event.action,
                    commentId = event.comment.id,
                    owner = event.repository.owner.login,
                    repo = event.repository.name,
                    pullNumber = event.issue.number,
                    author = event.comment.user.login,
                    body = event.comment.body,
                    filePath = null,
                    line = null,
                    threadId = null,
                    inThread = false,
                    updatedAt = Instant.parse(event.comment.updatedAt),
                )
            commentEventPublisher.publish(commentEvent)
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

    }
