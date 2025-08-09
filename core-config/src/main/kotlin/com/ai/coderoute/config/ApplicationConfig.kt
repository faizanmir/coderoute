package com.ai.coderoute.config

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ApplicationConfig {
    @Value("\${github.token}")
    private lateinit var githubToken: String

    @Value("\${github.base-url}")
    private lateinit var githubBaseUrl: String

    @Bean(name = ["GithubWebClient"])
    fun githubWebClient(): WebClient {
        return WebClient.builder().baseUrl(githubBaseUrl).defaultHeader("Accept", "application/vnd.github+json")
            .defaultHeader("Authorization", "Bearer $githubToken").build()
    }

    @Bean
    fun coroutineScope() =
        CoroutineScope(
            SupervisorJob() +
                    CoroutineExceptionHandler { _, t -> println("Exception encountered in coroutine scope ${t.message}") } +
                    Dispatchers.IO,
        )
}