package com.ai.coderoute.commentpersistence.service

import com.ai.coderoute.commentpersistence.entity.CommentEntity
import com.ai.coderoute.commentpersistence.entity.PullRequestEntity
import com.ai.coderoute.commentpersistence.repository.CommentRepository
import com.ai.coderoute.commentpersistence.repository.PullRequestRepository
import com.ai.coderoute.models.PullRequestCommentEvent
import org.springframework.stereotype.Service

@Service
class CommentPersistenceService(
    private val commentRepository: CommentRepository,
    private val pullRequestRepository: PullRequestRepository,
) {
    fun handle(event: PullRequestCommentEvent) {
        val pr =
            pullRequestRepository.findByOwnerAndRepoAndNumber(event.owner, event.repo, event.pullNumber)
                ?: pullRequestRepository.save(
                    PullRequestEntity(owner = event.owner, repo = event.repo, number = event.pullNumber),
                )
        when (event.action) {
            "created", "edited" -> {
                val entity = commentRepository.findById(event.commentId).orElse(CommentEntity(id = event.commentId, pullRequest = pr))
                entity.author = event.author
                entity.body = event.body ?: ""
                entity.path = event.filePath
                entity.line = event.line
                entity.threadId = event.threadId
                entity.inThread = event.inThread
                entity.updatedAt = event.updatedAt
                entity.pullRequest = pr
                commentRepository.save(entity)
            }
            "deleted" -> {
                commentRepository.findById(event.commentId).ifPresent { commentRepository.delete(it) }
            }
        }
    }
}
