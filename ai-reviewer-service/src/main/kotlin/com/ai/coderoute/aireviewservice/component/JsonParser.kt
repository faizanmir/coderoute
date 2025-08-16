package com.ai.coderoute.aireviewservice.component

import com.ai.coderoute.models.ReviewResponse
import com.fasterxml.jackson.databind.JsonNode
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
                val sanitised = sanitiseJson(json) ?: return null
                objectMapper.readValue(sanitised, ReviewResponse::class.java)
            } catch (e: Exception) {
                logger.info("Exception while parsing data", e)
                return null
            }
        } ?: run {
            return null
        }
    }

    private fun sanitiseJson(json: String): String? {
        return try {
            val arrayNode = objectMapper.readTree(json)
            if (!arrayNode.isArray) {
                logger.info("JSON is not an array")
                null
            } else {
                val root = objectMapper.createObjectNode()
                root.set<JsonNode>("reviews", arrayNode)
                objectMapper.writeValueAsString(root)
            }
        } catch (e: Exception) {
            logger.info("Exception while sanitising JSON", e)
            null
        }
    }
}
