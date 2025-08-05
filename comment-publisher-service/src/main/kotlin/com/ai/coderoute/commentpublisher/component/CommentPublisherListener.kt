package com.ai.coderoute.commentpublisher.component

import com.ai.coderoute.commentpublisher.service.GithubCommentPublisher
import com.ai.coderoute.commentpublisher.utils.ReviewCommentMapper
import com.ai.coderoute.models.AnalysisCompleted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class CommentPublisherListener
    @Autowired
    constructor(
        private val githubCommentPublisher: GithubCommentPublisher,
        private val coroutineScope: CoroutineScope,
    ) {
        @KafkaListener(
            topics = ["review-results-events"],
            groupId = "comment-publisher-group",
        )
        fun onFileAnalysisComplete(event: AnalysisCompleted) {
            val findings = event.findings.map { ReviewCommentMapper.fromReview(it) }.toList()
            coroutineScope.launch { githubCommentPublisher.postReview(event.owner, event.repo, event.pullNumber, findings) }
        }
    }
