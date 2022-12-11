package no.morten.advent.day1

fun parseCalorieData(data: String): List<List<Int>> {
    return data.split("\n\n").map {
        it.split("\n").map { it.toInt() }
    }
}

fun findMostCalories(data: List<List<Int>>): Int {
    return data.maxOf { it.sum() }
}

fun findTop3Calories(data: List<List<Int>>): List<Int> {
    return data.sortedByDescending { it.sum() }.subList(0, 3).map { it.sum() }
}