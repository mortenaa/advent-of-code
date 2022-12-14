package no.morten.advent.day14

import no.morten.advent.util.readResourceFile
import kotlin.math.max
import kotlin.math.min

fun main() {
    val inputFile = "day14_test.txt"
    //val inputFile = "day14.txt"
    val input = readResourceFile(inputFile)
    val (lowPoint, cave) = parse(input)
    moveSand(lowPoint, cave)
}

fun moveSand(lowPoint: Int, cave: MutableSet<Pair<Int, Int>>) {
    val start = 500 to 0
    while (true) {
        var sand = start
        if (sand.first to sand.second + 1 !in cave)
            sand = sand.first to sand.second + 1
        else if (sand.first - 1 to sand.second - 1 !in cave)
            sand = sand.first - 1 to sand.second - 1
        else if (sand.first + 1 to sand.second - 1 !in cave)
            sand = sand.first + 1 to sand.second - 1
        else 
    }
}

fun parse(input: String): Pair<Int, MutableSet<Pair<Int, Int>>> {
    val map = HashSet<Pair<Int,Int>>().toMutableSet()
    var lowPoint = 0
    input.lines().forEach {
        it.split(" -> ").map {
            it.substringBefore(",").toInt() to it.substringAfter(",").toInt()
        }.zipWithNext { a, b ->
            println("$a->$b")
            if (a.second < lowPoint)
                lowPoint = a.second
            if (b.second < lowPoint)
                lowPoint = b.second
            if (a.first == b.first) {
                for (i in min(a.second, b.second) .. max(a.second, b.second))
                    map.add(a.first to i)
            }
            if (a.second == b.second) {
                for (i in min(a.first, b.first) .. max(a.first, b.first))
                    map.add(i to a.second)
            }

        }
    }
    return lowPoint to map
}