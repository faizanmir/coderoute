package com.ai.coderoute.commentpersistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "pull_requests",
    uniqueConstraints = [UniqueConstraint(columnNames = ["owner", "repo", "number"])],
)
class PullRequestEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var owner: String = "",
    var repo: String = "",
    var number: Int = 0,
)
