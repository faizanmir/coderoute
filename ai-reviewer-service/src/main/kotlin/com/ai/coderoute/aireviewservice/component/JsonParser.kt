package com.ai.coderoute.aireviewservice.component

import com.ai.coderoute.models.ReviewFinding
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class JsonParser(private val objectMapper: ObjectMapper) {
    fun parseFindingList(json: String?): List<ReviewFinding> {
        return json?.let {
            val typeRef = object : TypeReference<List<ReviewFinding>>() {}
            objectMapper.readValue(json, typeRef)
        } ?: run {
            return emptyList()
        }
    }
}
