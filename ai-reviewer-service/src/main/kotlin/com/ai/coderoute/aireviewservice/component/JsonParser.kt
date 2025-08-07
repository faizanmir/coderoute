package com.ai.coderoute.aireviewservice.component

import com.ai.coderoute.models.ReviewResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class JsonParser(private val objectMapper: ObjectMapper) {
    private val logger = LoggerFactory.getLogger(JsonParser::class.java)

    fun parseFindingList(json: String?): ReviewResponse? {
        return json?.let {
            try {
                logger.info("parsing json ...")
                objectMapper.readValue(sanitiseJson(json), ReviewResponse::class.java)
            } catch (e: Exception) {
                logger.info("Exception while parsing data", e)
                return null
            }
        } ?: run {
            return null
        }
    }

    private fun sanitiseJson(json: String): String {
        if (!json.isEmpty()) {
            val removeGunk = json.substringAfter("[").substringBeforeLast("]")
            return "{\"reviews\": [$removeGunk]}"
        } else {
            return ""
        }
    }
}
