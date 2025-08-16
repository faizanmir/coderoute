package com.ai.coderoute.commentpersistence.service

import com.ai.coderoute.commentpersistence.entity.PullRequestEntity
import com.ai.coderoute.commentpersistence.entity.ReviewThreadEntity
import com.ai.coderoute.commentpersistence.repository.PullRequestRepository
import com.ai.coderoute.commentpersistence.repository.ReviewThreadRepository
import com.ai.coderoute.models.PullRequestReviewThreadEvent
import org.springframework.stereotype.Service

@Service
class ReviewThreadPersistenceService(
    private val reviewThreadRepository: ReviewThreadRepository,
    private val pullRequestRepository: PullRequestRepository,
) {
    fun handle(event: PullRequestReviewThreadEvent) {
        val pr =
            pullRequestRepository.findByOwnerAndRepoAndNumber(event.owner, event.repo, event.pullNumber)
                ?: pullRequestRepository.save(
                    PullRequestEntity(owner = event.owner, repo = event.repo, number = event.pullNumber),
                )

        val thread =
            reviewThreadRepository.findById(event.threadId)
                .orElse(ReviewThreadEntity(id = event.threadId, pullRequest = pr))

        when (event.action) {
            "resolved" -> {
                if (!thread.resolved) {
                    pr.unresolvedCount = (pr.unresolvedCount - 1).coerceAtLeast(0)
                }
                thread.resolved = true
                thread.resolvedBy = event.resolvedBy
                thread.resolvedAt = event.resolvedAt
            }
            "unresolved", "reopened" -> {
                if (thread.resolved) {
                    pr.unresolvedCount = pr.unresolvedCount + 1
                }
                thread.resolved = false
                thread.resolvedBy = null
                thread.resolvedAt = null
            }
        }

        thread.pullRequest = pr
        reviewThreadRepository.save(thread)
        pullRequestRepository.save(pr)
    }
}
