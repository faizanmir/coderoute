package com.ai.coderoute.aireviewservice.config

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.ChatClientRequest
import org.springframework.ai.chat.client.ChatClientResponse
import org.springframework.ai.chat.client.advisor.api.CallAdvisor
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    @Bean
    fun chatClient(
        builder: ChatClient.Builder,
    ): ChatClient {
        return builder.build()
    }

    @Bean
    fun noopCallAdvisor(): CallAdvisor {
        return object : CallAdvisor {
            override fun adviseCall(
                chatClientRequest: ChatClientRequest,
                callAdvisorChain: CallAdvisorChain,
            ): ChatClientResponse {
                return callAdvisorChain.nextCall(chatClientRequest)
            }

            override fun getName(): String {
                return "NoOpCallAdvisor"
            }

            override fun getOrder(): Int {
                return Int.MAX_VALUE
            }
        }
    }
}
