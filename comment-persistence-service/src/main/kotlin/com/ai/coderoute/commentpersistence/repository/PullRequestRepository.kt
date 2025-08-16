package com.ai.coderoute.commentpersistence.repository

import com.ai.coderoute.commentpersistence.entity.PullRequestEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PullRequestRepository : JpaRepository<PullRequestEntity, Long> {
    fun findByOwnerAndRepoAndNumber(owner: String, repo: String, number: Int): PullRequestEntity?
}
