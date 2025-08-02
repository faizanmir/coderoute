package com.ai.coderoute.webhookhandler.controller

import com.ai.coderoute.models.PullRequestReceivedEvent
import com.ai.coderoute.webhookhandler.models.GitHubPullRequestPayload
import com.ai.coderoute.webhookhandler.service.WebhookEventPublisher
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/api/v1")
class GithubWebhookController @Autowired constructor(private val eventPublisher: WebhookEventPublisher) {

    private val logger = LoggerFactory.getLogger(GithubWebhookController::class.java)

    @RequestMapping("/webhook")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun handleWebhook(@RequestParam payload: GitHubPullRequestPayload) {
        logger.info("Event received ...")
        logger.info("Received webhook for action '${payload.action}' on PR #${payload.pullRequest.number}")
        if (payload.action !in listOf("opened", "synchronize")) {
            logger.info("Ignoring action '${payload.action}' as it is not relevant for a code review trigger.")
            return
        }
        val event = PullRequestReceivedEvent(
            owner = payload.repository.owner.login,
            repo = payload.repository.name,
            pullNumber = payload.pullRequest.number,
            cloneUrl = payload.repository.cloneUrl,
            baseSha = payload.pullRequest.base.sha,
            headSha = payload.pullRequest.head.sha
        )
        eventPublisher.publish(event)
    }

}