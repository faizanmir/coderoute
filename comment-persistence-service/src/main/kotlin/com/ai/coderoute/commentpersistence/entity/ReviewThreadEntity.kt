package com.ai.coderoute.commentpersistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "review_threads")
class ReviewThreadEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(optional = false)
    var pullRequest: PullRequestEntity? = null,

    var resolvedBy: String? = null,
    var resolvedAt: Instant? = null,
    var resolved: Boolean = false,
)
