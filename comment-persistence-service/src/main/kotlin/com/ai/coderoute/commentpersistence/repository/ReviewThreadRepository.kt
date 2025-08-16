package com.ai.coderoute.commentpersistence.repository

import com.ai.coderoute.commentpersistence.entity.ReviewThreadEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewThreadRepository : JpaRepository<ReviewThreadEntity, Long>
