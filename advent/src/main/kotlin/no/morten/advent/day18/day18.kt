package no.morten.advent.day18

import no.morten.advent.day17.parseMoves
import no.morten.advent.util.readResourceFile

fun main() {
    //val inputFile = "day18_test.txt"
    val inputFile = "day18.txt"
    val input = readResourceFile(inputFile)
    val droplets = parseDroplets(input)
    println(droplets)
    var sum = 0
    var (minX, maxX) = Int.MAX_VALUE to Int.MIN_VALUE
    var (minY, maxY) = Int.MAX_VALUE to Int.MIN_VALUE
    var (minZ, maxZ) = Int.MAX_VALUE to Int.MIN_VALUE

    for (d in droplets) {
        val (x, y, z) = d
        if (x < minX) minX = x; if (x > maxX) maxX = x
        if (y < minY) minY = y; if (y > maxY) maxY = y
        if (z < minZ) minZ = z; if (z > maxZ) maxZ = z

        if (Triple(x-1, y, z) !in droplets) sum += 1
        if (Triple(x-1, y, z) !in droplets) sum += 1
        if (Triple(x, y+1, z) !in droplets) sum += 1
        if (Triple(x, y-1, z) !in droplets) sum += 1
        if (Triple(x, y, z+1) !in droplets) sum += 1
        if (Triple(x, y, z-1) !in droplets) sum += 1
    }
    println("Part 1: $sum")
    println("x: $minX - $maxX")
    println("y: $minY - $maxY")
    println("z: $minZ - $maxZ")
    minX -= 1; maxX +=1
    minY -= 1; maxY +=1
    minZ -= 1; maxZ +=1

    val air = emptySet<Triple<Int, Int, Int>>().toMutableSet()
    val queue = ArrayDeque<Triple<Int, Int, Int>>()
    var current = Triple(maxX, maxY, maxZ)
    queue.addLast(current)
    air.add(current)

    fun validPoint(p: Triple<Int, Int, Int>) =
        p !in droplets &&
        p !in air &&
        p.first >= minX && p.first <= maxX &&
        p.second >= minY && p.second <= maxY &&
        p.third >= minZ && p.third <= maxZ

    while (queue.isNotEmpty()) {
        current = queue.removeFirst()
        for (p in listOf(
            current.copy(first = current.first + 1),
            current.copy(first = current.first - 1),
            current.copy(second = current.second + 1),
            current.copy(second = current.second - 1),
            current.copy(third = current.third + 1),
            current.copy(third = current.third - 1)
        )) {
            if (validPoint(p)) {
                air.add(p)
                queue.addLast(p)
            }
        }
    }

    println("Droplets: ${droplets.size}")
    println("Air:      ${air.size}")

    sum = 0
    for (d in droplets) {
        val (x, y, z) = d

        if (Triple(x-1, y, z) in air) sum += 1
        if (Triple(x-1, y, z) in air) sum += 1
        if (Triple(x, y+1, z) in air) sum += 1
        if (Triple(x, y-1, z) in air) sum += 1
        if (Triple(x, y, z+1) in air) sum += 1
        if (Triple(x, y, z-1) in air) sum += 1
    }
    println("Exposed: $sum")
}

fun parseDroplets(input: String): Set<Triple<Int,Int,Int>> =
    input.lines().map {
        it.trim()
            .split(",")
            .let { Triple(it[0].toInt(), it[1].toInt(), it[2].toInt()) } }.toSet()
