package com.ai.coderoute.commentpersistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "comments")
class CommentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var author: String = "",
    var updatedAt: Instant = Instant.EPOCH,
    var path: String? = null,
    var line: Int? = null,
    @Column(columnDefinition = "TEXT")
    var body: String = "",
    var threadId: Long? = null,
    var inThread: Boolean = false,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pull_request_id")
    var pullRequest: PullRequestEntity = PullRequestEntity(),
)
