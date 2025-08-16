package com.ai.coderoute.constants

object Events {
    object PR {
        const val RECEIVED = "pr.received"
        const val RECEIVED_KEY = "pr.received.key"

        object Analysis {
            const val COMPLETE = "pr.analysis.complete"
            const val COMPLETE_KEY = "pr.analysis.complete.key"
        }
    }

    object Review {
        const val REVIEW_COMPLETE = "ai.review.complete"
        const val REVIEW_COMPLETE_KEY = "ai.review.complete.key"
    }

    object Comment {
        const val RECEIVED = "pr.comment.received"
        const val RECEIVED_KEY = "pr.comment.received.key"
    }

    object ReviewThread {
        const val RECEIVED = "pr.review_thread.received"
        const val RECEIVED_KEY = "pr.review_thread.received.key"
    }
}
