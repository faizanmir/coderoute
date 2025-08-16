package com.ai.coderoute.commentpublisher.service

import com.ai.coderoute.models.CreateReviewRequest
import com.ai.coderoute.models.ReviewComment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodilessEntity

@Service
class GithubCommentPublisher
    @Autowired
    constructor(
        @Qualifier("GithubWebClient") private val webClient: WebClient,
    ) {
        suspend fun postReview(
            owner: String,
            repo: String,
            pullNumber: Int,
            comments: List<ReviewComment>,
        ) {
            if (comments.isEmpty()) {
                println("No comments to post for PR #$pullNumber.")
                return
            }

            val reviewRequest =
                CreateReviewRequest(
                    body = "Automated AI code review complete.",
                    comments = comments,
                )
            println("Review Request $reviewRequest")
            try {
                webClient.post().uri("/repos/{owner}/{repo}/pulls/{pullNumber}/reviews", owner, repo, pullNumber)
                    .bodyValue(reviewRequest).retrieve().awaitBodilessEntity()
                println("Successfully posted ${comments.size} comments to PR #$pullNumber.")
            } catch (e: Exception) {
                println("Failed to post review to PR #$pullNumber: ${e.message}")
            }
        }
    }
