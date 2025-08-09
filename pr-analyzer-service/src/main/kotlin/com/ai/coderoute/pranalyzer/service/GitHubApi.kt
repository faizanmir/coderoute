package com.ai.coderoute.pranalyzer.service

import com.ai.coderoute.pranalyzer.models.GitHubComparison
import com.ai.coderoute.pranalyzer.models.GitHubFileContent
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Service
class GitHubApi(
    @Qualifier("GithubWebClient") private val webClient: WebClient,
) {
    fun compareCommits(
        owner: String,
        repo: String,
        baseSha: String,
        headSha: String,
    ): Mono<GitHubComparison> {
        return webClient.get().uri("/repos/{owner}/{repo}/compare/{baseSha}...{headSha}", owner, repo, baseSha, headSha)
            .retrieve().bodyToMono<GitHubComparison>()
    }

    fun getFileContent(
        owner: String,
        repo: String,
        path: String,
        ref: String,
    ): Mono<GitHubFileContent> {
        return webClient.get().uri("/repos/{owner}/{repo}/contents/{path}?ref={ref}", owner, repo, path, ref)
            .retrieve().bodyToMono<GitHubFileContent>()
    }
}
