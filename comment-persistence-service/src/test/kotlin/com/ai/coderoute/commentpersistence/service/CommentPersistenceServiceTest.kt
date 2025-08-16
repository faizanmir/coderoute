package com.ai.coderoute.commentpersistence.service

import com.ai.coderoute.commentpersistence.repository.CommentRepository
import com.ai.coderoute.commentpersistence.repository.PullRequestRepository
import com.ai.coderoute.models.PullRequestCommentEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.Instant

@DataJpaTest
class CommentPersistenceServiceTest @Autowired constructor(
    private val commentRepository: CommentRepository,
    private val pullRequestRepository: PullRequestRepository,
) {
    private val service = CommentPersistenceService(commentRepository, pullRequestRepository)

    @Test
    fun `duplicate comment events do not create multiple records`() {
        val event =
            PullRequestCommentEvent(
                action = "created",
                commentId = 1,
                owner = "owner",
                repo = "repo",
                pullNumber = 1,
                author = "user",
                body = "body",
                filePath = "file",
                line = 1,
                threadId = 1,
                inThread = false,
                updatedAt = Instant.now(),
            )
        service.handle(event)
        service.handle(event)
        assertEquals(1, commentRepository.count())
        assertEquals(1, pullRequestRepository.count())
        val saved = commentRepository.findById(1).get()
        assertEquals(1, saved.threadId)
    }
}
