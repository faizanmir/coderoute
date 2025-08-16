package com.ai.coderoute.commentpersistence.service

import com.ai.coderoute.commentpersistence.repository.PullRequestRepository
import com.ai.coderoute.commentpersistence.repository.ReviewThreadRepository
import com.ai.coderoute.models.PullRequestReviewThreadEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.Instant

@DataJpaTest
class ReviewThreadPersistenceServiceTest @Autowired constructor(
    private val reviewThreadRepository: ReviewThreadRepository,
    private val pullRequestRepository: PullRequestRepository,
) {
    private val service = ReviewThreadPersistenceService(reviewThreadRepository, pullRequestRepository)

    @Test
    fun `unresolved and resolved events adjust counts`() {
        val reopenEvent =
            PullRequestReviewThreadEvent(
                action = "reopened",
                threadId = 1,
                owner = "owner",
                repo = "repo",
                pullNumber = 1,
                resolvedBy = null,
                resolvedAt = null,
            )
        service.handle(reopenEvent)

        var pr = pullRequestRepository.findByOwnerAndRepoAndNumber("owner", "repo", 1)!!
        assertEquals(1, pr.unresolvedCount)

        val resolvedEvent =
            PullRequestReviewThreadEvent(
                action = "resolved",
                threadId = 1,
                owner = "owner",
                repo = "repo",
                pullNumber = 1,
                resolvedBy = "user",
                resolvedAt = Instant.now(),
            )
        service.handle(resolvedEvent)

        val thread = reviewThreadRepository.findById(1).get()
        pr = pullRequestRepository.findByOwnerAndRepoAndNumber("owner", "repo", 1)!!
        assertEquals(0, pr.unresolvedCount)
        assertEquals(true, thread.resolved)
        assertEquals("user", thread.resolvedBy)
    }
}
