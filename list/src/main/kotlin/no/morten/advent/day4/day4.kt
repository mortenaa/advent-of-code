import no.morten.advent.day3.priority
import java.io.File
import java.lang.Math.max

fun part1(): Int {
    val input = read("list/src/main/resources/day4.txt")!!
        .split("\n")
        .map {
            it.split(",")
                .map { toRange(it) }.let { it[0] to it[1] }
        }
    println(input)
    return input.count { (it.first union it.second).size == max(it.first.size(), it.second.size()) }
}

fun part2(): Int {
    val input = read("list/src/main/resources/day4.txt")!!
        .split("\n")
        .map {
            it.split(",")
                .map { toRange(it) }.let { it[0] to it[1] }
        }
    println(input)
    return input.count { (it.first intersect it.second).isNotEmpty() }
}

fun IntRange.size() =
    endInclusive - start + 1

fun toRange(s: String): IntRange =
    s.split("-").let {
        IntRange(it[0].toInt(), it[1].toInt())
    }


fun read(path: String): String =
    File(path).readText()

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}