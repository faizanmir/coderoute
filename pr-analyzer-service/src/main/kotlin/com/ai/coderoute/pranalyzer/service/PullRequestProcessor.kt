package com.ai.coderoute.pranalyzer.service

import com.ai.coderoute.constants.Events
import com.ai.coderoute.logging.logger
import com.ai.coderoute.models.FileReadyForAnalysis
import com.ai.coderoute.models.PullRequestReceivedEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.Base64

@Service
class PullRequestProcessor(
    private val kafkaTemplate: KafkaTemplate<String, FileReadyForAnalysis>,
    private val githubApi: GitHubApi,
) {
    private val topic = Events.PR.Analysis.COMPLETE
    private val logger = logger<PullRequestProcessor>()

    fun process(event: PullRequestReceivedEvent) {
        try {
            githubApi.compareCommits(event.owner, event.repo, event.baseSha, event.headSha).subscribe { diff ->
                diff.files.filter { it.status != "removed" }.forEach { changedFile ->
                    githubApi.getFileContent(event.owner, event.repo, changedFile.filename, event.headSha)
                        .subscribe { fileContent ->
                            val decodedContent =
                                Base64.getDecoder().decode(fileContent.content).toString(Charsets.UTF_8)
                            val numberedContent =
                                decodedContent.lines().mapIndexed { index, line -> "${index + 1}: $line" }
                                    .joinToString("\n")

                            val outgoingEvent =
                                FileReadyForAnalysis(
                                    filename = changedFile.filename,
                                    contentWithLineNumbers = numberedContent,
                                    repo = event.repo,
                                    pullNumber = event.pullNumber,
                                    owner = event.owner,
                                )

                            logger.info("Outgoing Event {}", outgoingEvent)

                            kafkaTemplate.send(topic, Events.PR.Analysis.COMPLETE_KEY, outgoingEvent)
                        }
                }
            }
        } catch (e: Exception) {
            logger.error("Failed to process PR #${event.pullNumber} using API.", e)
        }
    }
}
