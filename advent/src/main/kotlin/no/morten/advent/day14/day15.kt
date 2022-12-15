package no.morten.advent.day14

import no.morten.advent.util.readResourceFile
import kotlin.math.max
import kotlin.math.min

fun main() {
    //val inputFile = "day14_test.txt"
    val inputFile = "day14.txt"
    val input = readResourceFile(inputFile)
    var (lowPoint, cave) = parse(input)
    println(lowPoint)
    var count = 0
    do  {
        var (done, cave) = insertSand2(lowPoint, cave)
        count+=1
    } while (500 to 0 !in cave)
    println(count)
}

fun insertSand(lowPoint: Int, cave: MutableSet<Pair<Int, Int>>): Pair<Boolean, MutableSet<Pair<Int, Int>>> {
    var sand = 500 to 0
    while (true) {
        println(sand)
        if (sand.first to sand.second + 1 !in cave)
            sand = sand.first to sand.second + 1
        else if (sand.first - 1 to sand.second + 1 !in cave)
            sand = sand.first - 1 to sand.second + 1
        else if (sand.first + 1 to sand.second + 1 !in cave)
            sand = sand.first + 1 to sand.second + 1
        else {
            cave.add(sand)
            return false to cave
        }
        if (sand.second > lowPoint)
            return true to cave

    }
}

fun insertSand2(lowPoint: Int, cave: MutableSet<Pair<Int, Int>>): Pair<Boolean, MutableSet<Pair<Int, Int>>> {
    val floor = lowPoint + 2
    var sand = 500 to 0
    while (true) {
        if (sand.first to sand.second + 1 !in cave)
            sand = sand.first to sand.second + 1
        else if (sand.first - 1 to sand.second + 1 !in cave)
            sand = sand.first - 1 to sand.second + 1
        else if (sand.first + 1 to sand.second + 1 !in cave)
            sand = sand.first + 1 to sand.second + 1
        else {
            cave.add(sand)
            return false to cave
        }
        if (sand.second + 1 == floor) {
            cave.add(sand)
            return false to cave
        }

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
            if (a.second > lowPoint)
                lowPoint = a.second
            if (b.second > lowPoint)
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