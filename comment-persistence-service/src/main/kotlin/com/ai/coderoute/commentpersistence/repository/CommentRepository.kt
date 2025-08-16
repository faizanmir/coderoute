package com.ai.coderoute.commentpersistence.repository

import com.ai.coderoute.commentpersistence.entity.CommentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<CommentEntity, Long>
