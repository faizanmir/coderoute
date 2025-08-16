package com.ai.coderoute.webhookhandler.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GitHubPullRequestPayload(
    @JsonProperty("action")
    val action: String?,
    @JsonProperty("repository")
    val repository: Repository,
    @JsonProperty("pull_request")
    val pullRequest: PullRequest,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Repository(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("owner")
    val owner: Owner,
    @JsonProperty("clone_url")
    val cloneUrl: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Owner(
    @JsonProperty("login")
    val login: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class PullRequest(
    @JsonProperty("number")
    val number: Int,
    @JsonProperty("head")
    val head: Head,
    @JsonProperty("base")
    val base: Base,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Base(
    @JsonProperty("sha")
    val sha: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Head(
    @JsonProperty("sha")
    val sha: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GitHubReviewCommentPayload(
    @JsonProperty("action") val action: String,
    @JsonProperty("repository") val repository: Repository,
    @JsonProperty("pull_request") val pullRequest: PullRequest,
    @JsonProperty("comment") val comment: ReviewComment,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReviewComment(
    @JsonProperty("id") val id: Long,
    @JsonProperty("user") val user: User,
    @JsonProperty("body") val body: String,
    @JsonProperty("path") val path: String? = null,
    @JsonProperty("line") val line: Int? = null,
    @JsonProperty("in_reply_to_id") val inReplyToId: Long? = null,
    @JsonProperty("created_at") val createdAt: String,
    @JsonProperty("updated_at") val updatedAt: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GitHubIssueCommentPayload(
    @JsonProperty("action") val action: String,
    @JsonProperty("repository") val repository: Repository,
    @JsonProperty("issue") val issue: Issue,
    @JsonProperty("comment") val comment: IssueComment,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class IssueComment(
    @JsonProperty("id") val id: Long,
    @JsonProperty("user") val user: User,
    @JsonProperty("body") val body: String,
    @JsonProperty("created_at") val createdAt: String,
    @JsonProperty("updated_at") val updatedAt: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class User(
    @JsonProperty("login") val login: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Issue(
    @JsonProperty("number") val number: Int,
    @JsonProperty("pull_request") val pullRequest: Any? = null,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GitHubReviewThreadPayload(
    @JsonProperty("action") val action: String,
    @JsonProperty("repository") val repository: Repository,
    @JsonProperty("pull_request") val pullRequest: PullRequest,
    @JsonProperty("thread") val thread: ReviewThread,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReviewThread(
    @JsonProperty("id") val id: Long,
    @JsonProperty("resolved") val resolved: Boolean? = null,
    @JsonProperty("resolved_by") val resolvedBy: User? = null,
    @JsonProperty("resolved_at") val resolvedAt: String? = null,
)
