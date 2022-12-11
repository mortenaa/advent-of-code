package no.morten.advent.day3

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day2Test {

    @Test
    fun priorityTest() {
        assertEquals(1,'a'.priority())
        assertEquals(27,'A'.priority())
    }

    @Test
    fun testParts() {
        val input = "PmmdzqPrVvPwwTWBwg"
        val (a, b) = input.compartments()
        assertEquals("PmmdzqPrV", a)
        assertEquals("vPwwTWBwg", b)
    }

    @Test
    fun testPriority() {
        val input = "PmmdzqPrVvPwwTWBwg"
        assertEquals(42, priority(input))
    }
}