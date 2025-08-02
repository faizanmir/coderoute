package com.ai.coderoute.webhookhandler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebHookHandlerApplication

fun main(args : Array<String>) {
    runApplication<WebHookHandlerApplication>(*args)
}