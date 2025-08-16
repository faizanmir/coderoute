package com.ai.coderoute.commentpersistence.controller

import com.ai.coderoute.commentpersistence.entity.CommentEntity
import com.ai.coderoute.commentpersistence.repository.CommentRepository
import com.ai.coderoute.commentpersistence.repository.PullRequestRepository
import com.ai.coderoute.commentpersistence.repository.ReviewThreadRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/pr")
class PullRequestController(
    private val pullRequestRepository: PullRequestRepository,
    private val reviewThreadRepository: ReviewThreadRepository,
    private val commentRepository: CommentRepository,
) {
    @GetMapping("/{owner}/{repo}/{number}/unresolved-count")
    fun unresolvedCount(
        @PathVariable owner: String,
        @PathVariable repo: String,
        @PathVariable number: Int,
    ): UnresolvedCountResponse {
        val pr = pullRequestRepository.findByOwnerAndRepoAndNumber(owner, repo, number)
        return UnresolvedCountResponse(pr?.unresolvedCount ?: 0)
    }

    @GetMapping("/{owner}/{repo}/{number}/threads")
    fun threads(
        @PathVariable owner: String,
        @PathVariable repo: String,
        @PathVariable number: Int,
    ): List<ReviewThreadResponse> {
        val threads = reviewThreadRepository.findByPullRequestOwnerAndPullRequestRepoAndPullRequestNumber(owner, repo, number)
        return threads.map { thread ->
            val comments = commentRepository
                .findByPullRequestOwnerAndPullRequestRepoAndPullRequestNumberAndThreadId(owner, repo, number, thread.id)
                .map { it.toDto() }
            ReviewThreadResponse(
                id = thread.id,
                resolved = thread.resolved,
                resolvedBy = thread.resolvedBy,
                resolvedAt = thread.resolvedAt,
                comments = comments,
            )
        }
    }

    private fun CommentEntity.toDto() =
        CommentResponse(
            id = id,
            author = author,
            body = body,
            path = path,
            line = line,
            updatedAt = updatedAt,
        )
}

data class ReviewThreadResponse(
    val id: Long,
    val resolved: Boolean,
    val resolvedBy: String?,
    val resolvedAt: java.time.Instant?,
    val comments: List<CommentResponse>,
)

data class CommentResponse(
    val id: Long,
    val author: String,
    val body: String,
    val path: String?,
    val line: Int?,
    val updatedAt: java.time.Instant,
)

data class UnresolvedCountResponse(val unresolvedCount: Int)

