package no.morten.advent.day1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day1Test {

    @Test
    fun testDataParser() {
        val raw_data = getResourceAsText("/day1/sample_data.txt")  ?: throw IllegalArgumentException()
        val parsed_data = parseCalorieData(raw_data)
        assertNotNull(parsed_data)
        assertEquals(5, parsed_data.size)
        assertEquals(4000, parsed_data[1][0])
    }

    @Test
    fun testPart1WithSampleData() {
        val raw_data = getResourceAsText("/day1/sample_data.txt")  ?: throw IllegalArgumentException()
        val parsed_data = parseCalorieData(raw_data)
        val mostCalories = findMostCalories(parsed_data)
        assertEquals(24000, mostCalories)
    }

    @Test
    fun testPart1() {
        val raw_data = getResourceAsText("/day1/real_data.txt")  ?: throw IllegalArgumentException()
        val parsed_data = parseCalorieData(raw_data)
        val mostCalories = findMostCalories(parsed_data)
        assertEquals(66186, mostCalories)
    }

    @Test
    fun testPart2WithSampleData() {
        val raw_data = getResourceAsText("/day1/sample_data.txt")  ?: throw IllegalArgumentException()
        val parsed_data = parseCalorieData(raw_data)
        val mostCalories = findTop3Calories(parsed_data)
        assertEquals(45000, mostCalories.sum())
    }

    @Test
    fun testPart2WithRealData() {
        val raw_data = getResourceAsText("/day1/real_data.txt")  ?: throw IllegalArgumentException()
        val parsed_data = parseCalorieData(raw_data)
        val mostCalories = findTop3Calories(parsed_data)
        assertEquals(196804, mostCalories.sum())
    }




    fun getResourceAsText(path: String): String? =
        object {}.javaClass.getResource(path)?.readText()

}