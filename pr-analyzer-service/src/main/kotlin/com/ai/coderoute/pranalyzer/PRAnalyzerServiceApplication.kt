package com.ai.coderoute.pranalyzer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
class PRAnalyzerServiceApplication

fun main(args: Array<String>) {
    runApplication<PRAnalyzerServiceApplication>(* args)
}
