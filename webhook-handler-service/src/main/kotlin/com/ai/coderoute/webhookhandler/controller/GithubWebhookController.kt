package com.ai.coderoute.webhookhandler.controller

import com.ai.coderoute.webhookhandler.service.GitHubWebhookService
import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/api/v1")
class GithubWebhookController
    @Autowired
    constructor(private val webhookService: GitHubWebhookService) {
        private val logger = LoggerFactory.getLogger(GithubWebhookController::class.java)

        @PostMapping("/webhook")
        @ResponseStatus(HttpStatus.OK)
        fun handleWebhook(
            @RequestHeader("X-GitHub-Event") eventType: String,
            @RequestBody jsonNode: JsonNode,
        ) {
            logger.info("Received event {}", jsonNode)
            when (eventType) {
                "ping" -> webhookService.handlePing(jsonNode)
                "pull_request" -> webhookService.handlePullRequest(jsonNode)
                "push"-> webhookService.handlePush(jsonNode)
                else -> println("Unhandled event: $eventType")
            }
        }
    }
