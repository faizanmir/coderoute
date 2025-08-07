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
data class GithubPushRequestPayload(
    @JsonProperty("repository")
    val repository: Repository,
    @JsonProperty("before")
    val before: String?,
    @JsonProperty("after")
    val after: String?
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
