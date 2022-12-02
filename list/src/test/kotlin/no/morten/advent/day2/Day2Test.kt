package no.morten.advent.day2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day2Test {

    @Test
    fun testDataParser() {
        val raw_data = getResourceAsText("/day2/sample_data.txt")  ?: throw IllegalArgumentException()
        val parsed_data = parseStrategyData(raw_data)
        assertNotNull(parsed_data)
        assertEquals(Move.Rock, parsed_data.first().move)
        assertEquals(Move.Scissor, parsed_data.last().response)
    }

    @Test
    fun testScore() {
        val raw_data = getResourceAsText("/day2/sample_data.txt")  ?: throw IllegalArgumentException()
        val parsed_data = parseStrategyData(raw_data)
        assertNotNull(parsed_data)
        assertEquals(15, score(parsed_data))
    }

    @Test
    fun testScore2() {
        val raw_data = getResourceAsText("/day2/sample_data.txt")  ?: throw IllegalArgumentException()
        val parsed_data = parseStrategyData2(raw_data)
        assertNotNull(parsed_data)
        assertEquals(12, score(parsed_data))
    }

    @Test
    fun testPart1() {
        val raw_data = getResourceAsText("/day2/real_data.txt")  ?: throw IllegalArgumentException()
        val parsed_data = parseStrategyData(raw_data)
        assertNotNull(parsed_data)
        assertEquals(11767, score(parsed_data))
    }

    @Test
    fun testPart2() {
        val raw_data = getResourceAsText("/day2/real_data.txt")  ?: throw IllegalArgumentException()
        val parsed_data = parseStrategyData2(raw_data)
        assertNotNull(parsed_data)
        assertEquals(13886, score(parsed_data))
    }

    fun getResourceAsText(path: String): String? =
        object {}.javaClass.getResource(path)?.readText()

}