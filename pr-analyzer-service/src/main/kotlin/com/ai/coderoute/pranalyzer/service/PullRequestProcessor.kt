package com.ai.coderoute.pranalyzer.service

import com.ai.coderoute.models.FileReadyForAnalysis
import com.ai.coderoute.models.PullRequestReceivedEvent
import com.ai.coderoute.pranalyzer.models.ProcessedFile
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.treewalk.CanonicalTreeParser
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files

@Service
class PullRequestProcessor(
    private val kafkaTemplate: KafkaTemplate<String, FileReadyForAnalysis>,
) {
    private val logger = LoggerFactory.getLogger(PullRequestProcessor::class.java)
    private val topic = "file-analysis-events"

    fun process(event: PullRequestReceivedEvent) {
        val localPath = Files.createTempDirectory("pr-repo-${event.pullNumber}-").toFile()
        var git: Git? = null

        try {
            logger.info("Cloning repository from ${event.cloneUrl} to temporary path $localPath")
            git = Git.cloneRepository().setURI(event.cloneUrl).setDirectory(localPath).call()

            val repository = git.repository
            val oldTreeId = repository.resolve("${event.baseSha}^{tree}")
            val newTreeId = repository.resolve("${event.headSha}^{tree}")

            repository.newObjectReader().use { reader ->
                val oldTreeIter = CanonicalTreeParser(null, reader, oldTreeId)
                val newTreeIter = CanonicalTreeParser(null, reader, newTreeId)

                logger.info("Performing local diff between base (${event.baseSha}) and head (${event.headSha})")
                val diffs: List<DiffEntry> = git.diff().setOldTree(oldTreeIter).setNewTree(newTreeIter).call()

                diffs.forEach { diffEntry ->
                    val processedFile = processDiffEntry(diffEntry, localPath)

                    val outgoingEvent =
                        FileReadyForAnalysis(
                            owner = event.owner,
                            repo = event.repo,
                            pullNumber = event.pullNumber,
                            filename = processedFile.filename,
                            contentWithLineNumbers = processedFile.content,
                        )

                    // Publish the event to the next topic in the chain
                    kafkaTemplate.send(topic, outgoingEvent)
                    logger.info("Published analysis request for file: ${processedFile.filename}")
                }
            }
        } catch (e: Exception) {
            logger.error("Failed to process PR #${event.pullNumber} by cloning.", e)
        } finally {
            logger.info("Cleaning up temporary directory: $localPath")
            git?.close()
            localPath.deleteRecursively()
        }
    }

    private fun processDiffEntry(
        diffEntry: DiffEntry,
        localPath: File,
    ): ProcessedFile {
        val filename =
            when (diffEntry.changeType) {
                DiffEntry.ChangeType.DELETE -> diffEntry.oldPath
                else -> diffEntry.newPath
            }

        return if (diffEntry.changeType == DiffEntry.ChangeType.DELETE) {
            ProcessedFile(filename, "File was removed in this pull request.")
        } else {
            val localFile = File(localPath, filename)
            if (!localFile.exists()) {
                logger.warn("File listed in diff does not exist locally: $filename")
                ProcessedFile(filename, "File does not exist at head commit.")
            } else {
                val numberedContent = localFile.readLines(Charsets.UTF_8)
                    .mapIndexed { index, line -> "${index + 1}: $line" }
                    .joinToString("\n")
                logger.info("numberedContent {}", numberedContent)
                ProcessedFile(filename, numberedContent)
            }
        }
    }
}
