package com.ai.coderoute.commentpersistence.listener

import com.ai.coderoute.commentpersistence.service.ReviewThreadPersistenceService
import com.ai.coderoute.constants.Events
import com.ai.coderoute.models.PullRequestReviewThreadEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ReviewThreadEventListener(
    private val service: ReviewThreadPersistenceService,
) {
    @KafkaListener(topics = [Events.ReviewThread.RECEIVED], groupId = "comment-persistence-group")
    fun onReviewThread(event: PullRequestReviewThreadEvent) {
        service.handle(event)
    }
}
