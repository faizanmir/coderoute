package com.ai.coderoute.aireviewservice.component

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class JsonParserTest {
    private val objectMapper = jacksonObjectMapper()
    private val parser = JsonParser(objectMapper)

    @Test
    fun `parseFindingList returns ReviewResponse for valid array`() {
        val json = """[{"filePath":"src/App.kt","lineNumber":1,"severity":"HIGH","category":"STYLE","ruleId":"R1","message":"msg","suggestion":"sug"}]"""
        val result = parser.parseFindingList(json)
        assertNotNull(result)
        assertEquals(1, result!!.reviews.size)
        assertEquals("src/App.kt", result.reviews[0].filePath)
    }

    @Test
    fun `parseFindingList returns null for malformed json`() {
        val malformed = """[{"filePath":"src/App.kt""" // missing closing braces
        val result = parser.parseFindingList(malformed)
        assertNull(result)
    }

    @Test
    fun `parseFindingList returns null for non array input`() {
        val jsonObject = """{"filePath":"src/App.kt"}"""
        val result = parser.parseFindingList(jsonObject)
        assertNull(result)
    }
}
