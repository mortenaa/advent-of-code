package no.morten.advent.day12

import no.morten.advent.util.readResourceFile

fun main() {
    val inputFile = "day12_test.txt"
    //val inputFile = "day12.txt"
    val input = readResourceFile(inputFile)
    val (map, start, end) = parseMap(input)
    printMap(map)
    println(start to end)
}

fun printMap(map: Array<IntArray>) {
    for (i in map) {
        for (j in i) {
            print(String.format("%03d ", j))
        }
        println()
    }
}

fun parseMap(input: String): Triple<Array<IntArray>, Pair<Int, Int>, Pair<Int, Int>> {
    var start = 0 to 0
    var end = 0 to 0
    val map = input.lines().mapIndexed { i, r ->
        r.mapIndexed { j, c ->
            when {
                c == 'S' -> { start = i to j; 0 }
                c == 'E' -> { end = i to j; 'z'.code - 'a'.code}
                else -> { c.code - 'a'.code}
            }
        }.toIntArray()
    }.toTypedArray()
    assert(map[start.first][start.second] == 0) { "wrong start level" }
    assert(map[end.first][end.second] == 0) { "wrong target level" }
    return Triple(map, start, end)
}