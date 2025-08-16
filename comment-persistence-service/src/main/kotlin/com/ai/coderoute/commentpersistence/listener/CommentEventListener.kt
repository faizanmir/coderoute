package com.ai.coderoute.commentpersistence.listener

import com.ai.coderoute.commentpersistence.service.CommentPersistenceService
import com.ai.coderoute.constants.Events
import com.ai.coderoute.models.PullRequestCommentEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class CommentEventListener(
    private val service: CommentPersistenceService,
) {
    @KafkaListener(topics = [Events.Comment.RECEIVED], groupId = "comment-persistence-group")
    fun onComment(event: PullRequestCommentEvent) {
        service.handle(event)
    }
}
